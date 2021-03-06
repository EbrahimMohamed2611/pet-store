import { Component, OnInit } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js/pure';
import { environment } from '../../../environments/environment';

import { HttpErrorResponse } from "@angular/common/http";
import { ToastrService } from "ngx-toastr";
import { ShoppingCartService } from 'src/app/service/shoppingCart/shopping-cart.service';
import { CheckoutService } from 'src/app/service/checkout/checkout.service';
import { CustomerService } from 'src/app/service/customer/customer.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user/user.service';
import { Customer } from 'src/app/model/Customer.model';
import { CartItem } from 'src/app/model/CartItem.model';
import { Order } from 'src/app/model/Order.model';
import { Address } from 'src/app/model/Address.model';



@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  constructor(private customerService: CustomerService,
    private checkoutService: CheckoutService,
    private shoppingCartService: ShoppingCartService,
    private toasterService: ToastrService,
    private router: Router,
    private _userService: UserService
  ) {
  }

  public customer: Customer = new Customer();
  public cartItems: CartItem[] = [];
  public subTotal: number = 0;
  public disableOrEnableOrderButton: boolean;
  public isDefaultAddress: boolean = true;
  public order: Order = new Order();
  public payWithVisa = false;


  private stripePromise = loadStripe(environment.stripe);
  private userId = this._userService.getUserId();



  ngOnInit(): void {
    this.getShoppingCart();
    this.getCustomerDetails();
  }


  public getShoppingCart(): void {
    this.shoppingCartService.getShoppingCartForUser().subscribe((cartItems: CartItem[]) => {
      this.cartItems = cartItems;
      cartItems.forEach((item) => {
        this.subTotal += item.quantity * (item.product.price * (1 - item.product.discount));
      });
      //console.log(cartItems);
    }, (error: HttpErrorResponse) => {
      this.toasterService.error(error.error.message);
    });
  }

  private getCustomerDetails(): void {
    this.customerService.getCustomer(this.userId).subscribe((customer: Customer) => {
      this.customer = customer;
      if (this.customer.address == null) {
        this.customer.address = new Address();
        this.customer.address.city = "";
        this.customer.address.street = "";
        this.customer.address.country = "";

      }
      //console.log('this.customer ', this.customer);
    }, (error: HttpErrorResponse) => {

      this.toasterService.error(error.message);
    });

  }

  public agreeOnTermsAndCondition(): void {
    this.disableOrEnableOrderButton = !this.disableOrEnableOrderButton;
  }

  public shipToDifferentAddress(): void {
    this.isDefaultAddress = !this.isDefaultAddress;
  }

  public onPaymentMethodChange(payWithPaymentMethod: boolean): void {
    this.payWithVisa = payWithPaymentMethod;
  }

  public createOrder(): void {
    this.checkoutService.createOrder(this.order, this.userId).subscribe((orderResponse: Order) => {
      //console.log(orderResponse);

      this.toasterService.success('Your Order is Completed Please Check your Email For More Details');
    }, (error: HttpErrorResponse) => {
      //console.log(error.message);
    });
  }

  public placeOrder(): void {
    if (this.payWithVisa) {
      this.pay();
    } else {
      this.router.navigate(['/success']);
    }
  }

  async pay(): Promise<void> {
    const stripe = await this.stripePromise;
    this.order.address = this.customer.address;
    localStorage.setItem('customerOrder', JSON.stringify(this.order));
    this.checkoutService.payWithCart(this.userId)
      .subscribe((data: any) => {
        stripe?.redirectToCheckout({
          sessionId: data.id
        });
      });
  }
}

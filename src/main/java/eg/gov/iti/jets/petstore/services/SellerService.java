package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.ProductsDTO;
import eg.gov.iti.jets.petstore.dto.SellerDTO;
import eg.gov.iti.jets.petstore.dto.SellersDTO;

import eg.gov.iti.jets.petstore.dto.UserRegistrationDTO;

import eg.gov.iti.jets.petstore.dto.SellersDTO;


import java.util.List;

public interface SellerService {
    SellersDTO getAllSellers(Integer page, Integer pageLimit);

    SellerDTO getSeller(Long id);

    SellerDTO addSeller(SellerDTO seller);

    SellerDTO updateSeller(Long id, SellerDTO seller);

    void deleteSeller(Long id);

    void deleteAllSellers();

    void signUp(UserRegistrationDTO userRegistrationDTO);

    ProductsDTO getSellerProducts(Long id, Integer page, Integer pageLimit);
}

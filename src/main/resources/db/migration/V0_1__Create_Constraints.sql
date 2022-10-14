alter table order_info add constraint FK_OrderInfo_UserInfo_UserId foreign key (user_id) references user_info (id);
alter table order_items add constraint FK_OrderItems_OrderInfo_OrderId foreign key (order_id) references order_info (id);
alter table order_items add constraint FK_OrderItems_Product_ProductId foreign key (product_id) references product (id);
alter table user_info add constraint UQ_UserInfo_Email unique (email);
alter table product add constraint FK_Product_Brand_BrandId foreign key (brand_id) references brand (id);
alter table product add constraint FK_Product_Category_CategoryId foreign key (category_id) references category (id);
alter table product add constraint FK_Product_UserInfo_SellerId foreign key (seller_id) references user_info (id);
alter table product add constraint FK_Product_Species_SpeciesId foreign key (species) references species (id);
alter table product_image add constraint FK_ProductImage_Product_ProductId foreign key (product_id) references product (id);
alter table rate add constraint FK_Rate_Product_ProductId foreign key (product_id) references product (id);
alter table rate add constraint FK_Rate_UserInfo_UserId foreign key (user_id) references user_info (id);
alter table service add constraint FK_Service_UserInfo_ProviderId foreign key (provider_id) references user_info (id);
alter table service add constraint FK_Service_ServiceType_TypeId foreign key (type_id) references service_type (id);

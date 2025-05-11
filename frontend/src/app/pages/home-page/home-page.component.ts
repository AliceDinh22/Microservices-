import { UserDetails } from './../../model/order';
import { Component, inject, OnInit } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { Product } from '../../model/product';
import { ProductService } from '../../services/product.service';
import { Router } from '@angular/router';
import { Order } from '../../model/order';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './home-page.component.html',
  standalone: true,
  imports: [FormsModule],
  styleUrl: './home-page.component.css',
})
export class HomePageComponent implements OnInit {
  private readonly oidcSecurityService = inject(OidcSecurityService);
  private readonly productService = inject(ProductService);
  private readonly orderService = inject(OrderService);
  private readonly router = inject(Router);
  isAuthenticated = false;
  products: Array<Product> = [];
  quantityIsNull = false;
  orderSuccess = false;
  orderFailed = false;
  isAdmin = false;
  userDetails: any;

  ngOnInit(): void {
    this.oidcSecurityService.isAuthenticated$.subscribe(
      ({ isAuthenticated }) => {
        this.isAuthenticated = isAuthenticated;
        this.productService
          .getProducts()
          .pipe()
          .subscribe((product) => {
            this.products = product;
            console.log(this.products);
          });
      }
    );
    

    this.oidcSecurityService.userData$.subscribe((result) => {
      console.log(result);
      const email = result.userData.email;

      if (email === 'linhdinhyen22@gmail.com') this.isAdmin = true;
    });
  }

  goToCreateProductPage() {
    this.router.navigateByUrl('/add-product');
  }  

  orderProduct(product: Product, quantity: string) {
    this.oidcSecurityService.userData$.subscribe((result) => {
      console.log(result);
      const userDetails = {
        email: result.userData.email,
        firstName: result.userData.given_name,
        lastName: result.userData.family_name,
      };

      if (!quantity) {
        this.orderFailed = true;
        this.orderSuccess = false;
        this.quantityIsNull = true;
      } else {
        const order: Order = {
          skuCode: product.skuCode,
          name: product.name,
          price: product.price,
          quantity: Number(quantity),
          userDetails: userDetails,
        };

        this.orderService.orderProduct(order).subscribe(
          () => {
            this.orderSuccess = true;
          },
          (error) => {
            this.orderFailed = true;
          }
        );
      }
    });
  }
}

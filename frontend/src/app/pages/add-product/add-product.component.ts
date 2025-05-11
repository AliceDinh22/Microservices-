import { Inventory } from './../../model/inventory';
import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { InventoryService } from '../../services/inventory.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css',
})
export class AddProductComponent {
  addProductForm: FormGroup;
  private readonly productService = inject(ProductService);
  private readonly inventoryService = inject(InventoryService);
  selectedFiles: File[] = [];
  productCreated = false;

  constructor(private fb: FormBuilder) {
    this.addProductForm = this.fb.group({
      skuCode: ['', [Validators.required]],
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: [0, [Validators.required, Validators.min(0)]],
      quantity: [0, [Validators.required, Validators.min(0)]],
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.selectedFiles = Array.from(input.files);
    }
  }

  onSubmit(event: Event): void {
    event.preventDefault();

    if (this.addProductForm.invalid) {
      console.log('Form is not valid');
      return;
    }

    const formData = new FormData();
    formData.append('skuCode', this.addProductForm.get('skuCode')?.value);
    formData.append('name', this.addProductForm.get('name')?.value);
    formData.append(
      'description',
      this.addProductForm.get('description')?.value
    );
    formData.append(
      'price',
      this.addProductForm.get('price')?.value.toString()
    );

    for (const file of this.selectedFiles) {
      formData.append('files', file);
    }

    const inventory: Inventory = {
      skuCode: this.addProductForm.get('skuCode')?.value,
      name: this.addProductForm.get('name')?.value,
      quantity: this.addProductForm.get('quantity')?.value,
    };

    this.inventoryService.saveInventory(inventory).subscribe({
      next: (inventoryRes) => {
        console.log('Inventory saved:', inventoryRes);
      },
      error: (err) => console.error('Lỗi khi lưu inventory:', err),
    });
    
    this.productService.createProduct(formData).subscribe({
      next: (productRes) => {
        console.log('Product created:', productRes);
        this.productCreated = true;
        this.addProductForm.reset();
        this.selectedFiles = [];
      },
      error: (err) => console.error('Error creating product:', err),
    });
  }

  get skuCode() {
    return this.addProductForm.get('skuCode');
  }

  get name() {
    return this.addProductForm.get('name');
  }

  get description() {
    return this.addProductForm.get('description');
  }

  get price() {
    return this.addProductForm.get('price');
  }

  get quantity() {
    return this.addProductForm.get('quantity');
  }
}

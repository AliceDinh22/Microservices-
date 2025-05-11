import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Inventory } from '../model/inventory';

@Injectable({
  providedIn: 'root',
})
export class InventoryService {
  constructor(private httpClient: HttpClient) {}

  saveInventory(inventory: Inventory): Observable<Inventory> {
    return this.httpClient.post<Inventory>(
      'http://localhost:9000/api/inventory',
      inventory
    );
  }
}

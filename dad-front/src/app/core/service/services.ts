import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {resources} from '../resources/resources';

// Interfaces
export interface Category {
  id?: number;
  nombre: string;
  descripcion?: string;
}

export interface Product {
  id?: number;
  nombre: string;
  descripcion?: string;
  precio: number;
  categoriaId: number;
  categoriaDto?: Category;
}

export interface Inventory {
  id?: number;
  productoId: number;
  stockActual: number;
  stockMinimo: number;
  stockMaximo: number;
  costoPromedio?: number;
  ubicacion?: string;
  estado?: string;
}

export interface AuthUser {
  id?: number;
  userName: string;
  password: string;
}

export interface LoginRequest {
  userName: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  userName: string;
}

@Injectable({providedIn: 'root'})
export class Services {
  constructor(private http: HttpClient) {}

  // AUTH SERVICES
  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('auth/login', loginRequest);
  }

  register(user: AuthUser): Observable<AuthUser> {
    return this.http.post<AuthUser>('auth/create', user);
  }

  // CATEGORY SERVICES
  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(resources.catalogue.category);
  }

  getCategoryById(id: number): Observable<Category> {
    return this.http.get<Category>(`${resources.catalogue.category}/${id}`);
  }

  createCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(resources.catalogue.category, category);
  }

  updateCategory(id: number, category: Category): Observable<Category> {
    return this.http.put<Category>(`${resources.catalogue.category}/${id}`, category);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${resources.catalogue.category}/${id}`);
  }

  // PRODUCT SERVICES
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>('productos');
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`productos/${id}`);
  }

  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>('productos', product);
  }

  updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`productos/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`productos/${id}`);
  }

  // INVENTORY SERVICES
  getInventory(): Observable<Inventory[]> {
    return this.http.get<Inventory[]>('inventario');
  }

  getInventoryById(id: number): Observable<Inventory> {
    return this.http.get<Inventory>(`inventario/${id}`);
  }

  getInventoryByProductId(productoId: number): Observable<Inventory> {
    return this.http.get<Inventory>(`inventario/producto/${productoId}`);
  }

  createInventory(inventory: Inventory): Observable<Inventory> {
    return this.http.post<Inventory>('inventario', inventory);
  }

  updateInventory(id: number, inventory: Inventory): Observable<Inventory> {
    return this.http.put<Inventory>(`inventario/${id}`, inventory);
  }

  deleteInventory(id: number): Observable<void> {
    return this.http.delete<void>(`inventario/${id}`);
  }

  getLowStockProducts(): Observable<Inventory[]> {
    return this.http.get<Inventory[]>('inventario/stock-bajo');
  }

  getInventoryByLocation(location: string): Observable<Inventory[]> {
    return this.http.get<Inventory[]>(`inventario/ubicacion/${location}`);
  }
}

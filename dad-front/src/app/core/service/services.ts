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

export interface CarritoItem {
  id?: number;
  productoId: number;
  cantidad: number;
  precioUnitario: number;
  subtotal?: number;
  producto?: Product;
}

export interface Carrito {
  id?: number;
  clienteId?: number;
  sessionId?: string;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  total: number;
  activo: boolean;
  items: CarritoItem[];
}

export interface VentaItem {
  id?: number;
  productoId: number;
  productoNombre: string;
  cantidad: number;
  precioUnitario: number;
  descuentoItem: number;
  subtotal: number;
}

export interface Venta {
  id?: number;
  numeroVenta: string;
  clienteId?: number;
  carritoId: number;
  fechaVenta?: string;
  fechaActualizacion?: string;
  estado: string;
  subtotal: number;
  impuestos: number;
  descuento: number;
  total: number;
  observaciones?: string;
  items?: VentaItem[];
}

export interface CrearVentaRequest {
  clienteId?: number;
  sessionId?: string;
  carritoId: number;
  observaciones?: string;
}

export interface AgregarCarritoRequest {
  productoId: number;
  cantidad: number;
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

  // CARRITO SERVICES
  getCarritoByCliente(clienteId: number): Observable<Carrito> {
    return this.http.get<Carrito>(`carrito/cliente/${clienteId}`);
  }

  getCarritoBySession(sessionId: string): Observable<Carrito> {
    return this.http.get<Carrito>(`carrito/session/${sessionId}`);
  }

  agregarProductoAlCarrito(clienteId: number, request: AgregarCarritoRequest): Observable<Carrito> {
    return this.http.post<Carrito>(`carrito/cliente/${clienteId}/agregar`, request);
  }

  agregarProductoAlCarritoPorSession(sessionId: string, request: AgregarCarritoRequest): Observable<Carrito> {
    return this.http.post<Carrito>(`carrito/session/${sessionId}/agregar`, request);
  }

  actualizarCantidadItem(clienteId: number, productoId: number, cantidad: number): Observable<Carrito> {
    return this.http.put<Carrito>(`carrito/cliente/${clienteId}/producto/${productoId}`, { cantidad });
  }

  eliminarProductoDelCarrito(clienteId: number, productoId: number): Observable<Carrito> {
    return this.http.delete<Carrito>(`carrito/cliente/${clienteId}/producto/${productoId}`);
  }

  limpiarCarrito(clienteId: number): Observable<void> {
    return this.http.delete<void>(`carrito/cliente/${clienteId}/limpiar`);
  }

  // VENTAS SERVICES
  realizarVenta(request: CrearVentaRequest): Observable<Venta> {
    return this.http.post<Venta>('ventas/realizar', request);
  }

  getVentaById(ventaId: number): Observable<Venta> {
    return this.http.get<Venta>(`ventas/${ventaId}`);
  }

  getVentaByNumero(numeroVenta: string): Observable<Venta> {
    return this.http.get<Venta>(`ventas/numero/${numeroVenta}`);
  }

  getVentasByCliente(clienteId: number): Observable<Venta[]> {
    return this.http.get<Venta[]>(`ventas/cliente/${clienteId}`);
  }

  getAllVentas(): Observable<Venta[]> {
    return this.http.get<Venta[]>('ventas');
  }
}

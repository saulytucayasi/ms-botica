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

export interface OrdenCompra {
  id?: number;
  numeroOrden: string;
  proveedorId: number;
  fechaOrden?: string;
  fechaEntregaEsperada?: string;
  fechaActualizacion?: string;
  estado: string;
  subtotal: number;
  impuestos: number;
  total: number;
  observaciones?: string;
  proveedor?: Proveedor;
  detalles?: DetalleOrdenCompra[];
}

export interface DetalleOrdenCompra {
  id?: number;
  ordenCompraId: number;
  productoId: number;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  producto?: Product;
}

export interface Proveedor {
  id?: number;
  nombre: string;
  ruc: string;
  direccion?: string;
  telefono?: string;
  email?: string;
  contacto?: string;
  estado: string;
  fechaRegistro?: string;
  fechaActualizacion?: string;
}

export interface RecepcionMercancia {
  id?: number;
  numeroRecepcion: string;
  ordenCompraId: number;
  fechaRecepcion?: string;
  fechaActualizacion?: string;
  estado: string;
  observaciones?: string;
  ordenCompra?: OrdenCompra;
  detalles?: DetalleRecepcion[];
}

export interface DetalleRecepcion {
  id?: number;
  recepcionId: number;
  productoId: number;
  cantidadEsperada: number;
  cantidadRecibida: number;
  precioUnitario: number;
  subtotal: number;
  producto?: Product;
}

export interface CrearOrdenCompraRequest {
  proveedorId: number;
  fechaEntregaEsperada?: string;
  observaciones?: string;
  detalles: {
    productoId: number;
    cantidad: number;
    precioUnitario: number;
  }[];
}

export interface CrearRecepcionRequest {
  ordenCompraId: number;
  observaciones?: string;
  detalles: {
    productoId: number;
    cantidadEsperada: number;
    cantidadRecibida: number;
    precioUnitario: number;
  }[];
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

  // COMPRAS SERVICES

  // Ordenes de Compra
  getOrdenesCompra(): Observable<OrdenCompra[]> {
    return this.http.get<OrdenCompra[]>('compras/ordenes-compra');
  }

  getOrdenCompraById(id: number): Observable<OrdenCompra> {
    return this.http.get<OrdenCompra>(`compras/ordenes-compra/${id}`);
  }

  getOrdenCompraByNumero(numeroOrden: string): Observable<OrdenCompra> {
    return this.http.get<OrdenCompra>(`compras/ordenes-compra/numero/${numeroOrden}`);
  }

  getOrdenesCompraPorProveedor(proveedorId: number): Observable<OrdenCompra[]> {
    return this.http.get<OrdenCompra[]>(`compras/ordenes-compra/proveedor/${proveedorId}`);
  }

  getOrdenesCompraPorEstado(estado: string): Observable<OrdenCompra[]> {
    return this.http.get<OrdenCompra[]>(`compras/ordenes-compra/estado/${estado}`);
  }

  getOrdenesCompraVencidas(): Observable<OrdenCompra[]> {
    return this.http.get<OrdenCompra[]>('compras/ordenes-compra/vencidas');
  }

  createOrdenCompra(orden: CrearOrdenCompraRequest): Observable<OrdenCompra> {
    return this.http.post<OrdenCompra>('compras/ordenes-compra', orden);
  }

  updateOrdenCompra(id: number, orden: OrdenCompra): Observable<OrdenCompra> {
    return this.http.put<OrdenCompra>(`compras/ordenes-compra/${id}`, orden);
  }

  deleteOrdenCompra(id: number): Observable<void> {
    return this.http.delete<void>(`compras/ordenes-compra/${id}`);
  }

  cambiarEstadoOrdenCompra(id: number, estado: string): Observable<OrdenCompra> {
    return this.http.patch<OrdenCompra>(`compras/ordenes-compra/${id}/estado?estado=${estado}`, {});
  }

  // Proveedores
  getProveedores(): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>('compras/proveedores');
  }

  getProveedorById(id: number): Observable<Proveedor> {
    return this.http.get<Proveedor>(`compras/proveedores/${id}`);
  }

  getProveedorByRuc(ruc: string): Observable<Proveedor> {
    return this.http.get<Proveedor>(`compras/proveedores/ruc/${ruc}`);
  }

  buscarProveedoresPorNombre(nombre: string): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(`compras/proveedores/buscar?nombre=${nombre}`);
  }

  getProveedoresActivos(): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>('compras/proveedores/activos');
  }

  createProveedor(proveedor: Proveedor): Observable<Proveedor> {
    return this.http.post<Proveedor>('compras/proveedores', proveedor);
  }

  updateProveedor(id: number, proveedor: Proveedor): Observable<Proveedor> {
    return this.http.put<Proveedor>(`compras/proveedores/${id}`, proveedor);
  }

  deleteProveedor(id: number): Observable<void> {
    return this.http.delete<void>(`compras/proveedores/${id}`);
  }

  cambiarEstadoProveedor(id: number, estado: string): Observable<Proveedor> {
    return this.http.patch<Proveedor>(`compras/proveedores/${id}/estado?estado=${estado}`, {});
  }

  // Recepciones de Mercancia
  getRecepciones(): Observable<RecepcionMercancia[]> {
    return this.http.get<RecepcionMercancia[]>('compras/recepciones');
  }

  getRecepcionById(id: number): Observable<RecepcionMercancia> {
    return this.http.get<RecepcionMercancia>(`compras/recepciones/${id}`);
  }

  getRecepcionByNumero(numeroRecepcion: string): Observable<RecepcionMercancia> {
    return this.http.get<RecepcionMercancia>(`compras/recepciones/numero/${numeroRecepcion}`);
  }

  getRecepcionesPorOrdenCompra(ordenCompraId: number): Observable<RecepcionMercancia[]> {
    return this.http.get<RecepcionMercancia[]>(`compras/recepciones/orden-compra/${ordenCompraId}`);
  }

  getRecepcionesPorEstado(estado: string): Observable<RecepcionMercancia[]> {
    return this.http.get<RecepcionMercancia[]>(`compras/recepciones/estado/${estado}`);
  }

  createRecepcion(recepcion: CrearRecepcionRequest): Observable<RecepcionMercancia> {
    return this.http.post<RecepcionMercancia>('compras/recepciones', recepcion);
  }

  updateRecepcion(id: number, recepcion: RecepcionMercancia): Observable<RecepcionMercancia> {
    return this.http.put<RecepcionMercancia>(`compras/recepciones/${id}`, recepcion);
  }

  deleteRecepcion(id: number): Observable<void> {
    return this.http.delete<void>(`compras/recepciones/${id}`);
  }

  cambiarEstadoRecepcion(id: number, estado: string): Observable<RecepcionMercancia> {
    return this.http.patch<RecepcionMercancia>(`compras/recepciones/${id}/estado?estado=${estado}`, {});
  }

  procesarRecepcion(id: number): Observable<RecepcionMercancia> {
    return this.http.post<RecepcionMercancia>(`compras/recepciones/${id}/procesar`, {});
  }
}

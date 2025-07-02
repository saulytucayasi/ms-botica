import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CategoryComponent } from './category/category';
import { ProductsComponent } from './products/products.component';
import { InventoryComponent } from './inventory/inventory.component';
import { UsersComponent } from './users/users.component';
import { CarritoComponent } from './carrito/carrito.component';
import { VentasComponent } from './ventas/ventas.component';
import { ReciboComponent } from './recibo/recibo.component';
import { AuthGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { 
    path: 'dashboard', 
    component: DashboardComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'categories', 
    component: CategoryComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'products', 
    component: ProductsComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'inventory', 
    component: InventoryComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'users', 
    component: UsersComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'carrito', 
    component: CarritoComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'ventas', 
    component: VentasComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'recibo/:id', 
    component: ReciboComponent, 
    canActivate: [AuthGuard] 
  },
  { path: '**', redirectTo: '/login' }
];

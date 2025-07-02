import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-icon',
  standalone: true,
  imports: [CommonModule],
  template: `
    <svg 
      [class]="'icon ' + size + ' ' + (className || '')" 
      [style.color]="color"
      viewBox="0 0 24 24" 
      fill="currentColor"
    >
      <ng-container [ngSwitch]="name">
        
        <!-- Dashboard -->
        <path *ngSwitchCase="'dashboard'" d="M3 13h8V3H3v10zm0 8h8v-6H3v6zm10 0h8V11h-8v10zm0-18v6h8V3h-8z"/>
        
        <!-- Categories -->
        <path *ngSwitchCase="'categories'" d="M4 6H2v14c0 1.1.9 2 2 2h14v-2H4V6zm16-4H8c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-1 9H9V9h10v2zm-4 4H9v-2h6v2zm4-8H9V5h10v2z"/>
        
        <!-- Products -->
        <path *ngSwitchCase="'products'" d="M7 4V2C7 1.45 7.45 1 8 1H16C16.55 1 17 1.45 17 2V4H20C20.55 4 21 4.45 21 5S20.55 6 20 6H19V19C19 20.1 18.1 21 17 21H7C5.9 21 5 20.1 5 19V6H4C3.45 6 3 5.55 3 5S3.45 4 4 4H7ZM9 3V4H15V3H9ZM7 6V19H17V6H7Z"/>
        
        <!-- Inventory -->
        <path *ngSwitchCase="'inventory'" d="M20 2H4C3 2 2 3 2 4V16C2 17 3 18 4 18H8L12 22L16 18H20C21 18 22 17 22 16V4C22 3 21 2 20 2ZM20 16H15.17L12 19.17L8.83 16H4V4H20V16ZM7 9H17V11H7V9ZM7 12H14V14H7V12Z"/>
        
        <!-- Carrito -->
        <path *ngSwitchCase="'carrito'" d="M7 18C5.9 18 5.01 18.9 5.01 20S5.9 22 7 22 9 21.1 9 20 8.1 18 7 18ZM1 2V4H3L6.6 11.59L5.25 14.04C5.09 14.32 5 14.65 5 15C5 16.1 5.9 17 7 17H19V15H7.42C7.28 15 7.17 14.89 7.17 14.75L7.2 14.63L8.1 13H15.55C16.3 13 16.96 12.59 17.3 11.97L20.88 5H5.21L4.27 3H1ZM17 18C15.9 18 15.01 18.9 15.01 20S15.9 22 17 22 19 21.1 19 20 18.1 18 17 18Z"/>
        
        <!-- Ventas -->
        <path *ngSwitchCase="'ventas'" d="M7 15H9C9 16.08 10.37 17 12 17S15 16.08 15 15C15 13.9 13.96 13.5 11.76 12.97C9.64 12.44 7 11.78 7 9C7 7.21 8.47 5.69 10.5 5.18V3H13.5V5.18C15.53 5.69 17 7.21 17 9H15C15 7.92 13.63 7 12 7S9 7.92 9 9C9 10.1 10.04 10.5 12.24 11.03C14.36 11.56 17 12.22 17 15C17 16.79 15.53 18.31 13.5 18.82V21H10.5V18.82C8.47 18.31 7 16.79 7 15Z"/>
        
        <!-- Users -->
        <path *ngSwitchCase="'users'" d="M16 4C18.21 4 20 5.79 20 8S18.21 12 16 12 12 10.21 12 8 13.79 4 16 4ZM16 14C20.42 14 24 15.79 24 18V20H8V18C8 15.79 11.58 14 16 14ZM6 6V9H4V6H1V4H4V1H6V4H9V6H6ZM6 16.5C7.5 16.5 8.5 16.88 8.5 17.25V18H3.5V17.25C3.5 16.88 4.5 16.5 6 16.5ZM6 14.5C4.62 14.5 3.5 15.62 3.5 17S4.62 19.5 6 19.5 8.5 18.38 8.5 17 7.38 14.5 6 14.5Z"/>
        
        <!-- Add -->
        <path *ngSwitchCase="'add'" d="M19 13H13V19H11V13H5V11H11V5H13V11H19V13Z"/>
        
        <!-- Delete -->
        <path *ngSwitchCase="'delete'" d="M6 19C6 20.1 6.9 21 8 21H16C17.1 21 18 20.1 18 19V7H6V19ZM19 4H15.5L14.5 3H9.5L8.5 4H5V6H19V4Z"/>
        
        <!-- Edit -->
        <path *ngSwitchCase="'edit'" d="M3 17.25V21H6.75L17.81 9.94L14.06 6.19L3 17.25ZM20.71 7.04C21.1 6.65 21.1 6.02 20.71 5.63L18.37 3.29C17.98 2.9 17.35 2.9 16.96 3.29L15.13 5.12L18.88 8.87L20.71 7.04Z"/>
        
        <!-- Save -->
        <path *ngSwitchCase="'save'" d="M17 3H5C3.89 3 3 3.9 3 5V19C3 20.1 3.89 21 5 21H19C20.1 21 21 20.1 21 19V7L17 3ZM19 19H5V5H16.17L19 7.83V19ZM12 12C13.66 12 15 13.34 15 15S13.66 18 12 18 9 16.66 9 15 10.34 12 12 12ZM6 6H15V10H6V6Z"/>
        
        <!-- Search -->
        <path *ngSwitchCase="'search'" d="M15.5 14H14.71L14.43 13.73C15.41 12.59 16 11.11 16 9.5C16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16C11.11 16 12.59 15.41 13.73 14.43L14 14.71V15.5L19 20.49L20.49 19L15.5 14ZM9.5 14C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14Z"/>
        
        <!-- Close -->
        <path *ngSwitchCase="'close'" d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
        
        <!-- Print -->
        <path *ngSwitchCase="'print'" d="M19 8H5C3.34 8 2 9.34 2 11V17H6V21H18V17H22V11C22 9.34 20.66 8 19 8ZM16 19H8V14H16V19ZM19 12C18.45 12 18 11.55 18 11S18.45 10 19 10 20 10.45 20 11 19.55 12 19 12ZM18 3H6V7H18V3Z"/>
        
        <!-- Logout -->
        <path *ngSwitchCase="'logout'" d="M17 7L15.59 8.41 18.17 11H8V13H18.17L15.59 15.59 17 17L22 12L17 7ZM4 5H12V3H4C2.9 3 2 3.9 2 5V19C2 20.1 2.9 21 4 21H12V19H4V5Z"/>
        
        <!-- Check -->
        <path *ngSwitchCase="'check'" d="M9 16.17L4.83 12L3.41 13.41L9 19L21 7L19.59 5.59L9 16.17Z"/>
        
        <!-- Warning -->
        <path *ngSwitchCase="'warning'" d="M1 21H23L12 2L1 21ZM13 18H11V16H13V18ZM13 14H11V10H13V14Z"/>
        
        <!-- Info -->
        <path *ngSwitchCase="'info'" d="M12 2C6.48 2 2 6.48 2 12S6.48 22 12 22 22 17.52 22 12 17.52 2 12 2ZM13 17H11V11H13V17ZM13 9H11V7H13V9Z"/>
        
        <!-- Loading -->
        <path *ngSwitchCase="'loading'" d="M12 6V9L16 5L12 1V4C7.58 4 4 7.58 4 12S7.58 20 12 20 20 16.42 20 12H18C18 15.31 15.31 18 12 18S6 15.31 6 12 8.69 6 12 6Z">
          <animateTransform attributeName="transform" attributeType="XML" type="rotate" from="0 12 12" to="360 12 12" dur="1s" repeatCount="indefinite"/>
        </path>
        
        <!-- Success -->
        <path *ngSwitchCase="'success'" d="M12 2C6.48 2 2 6.48 2 12S6.48 22 12 22 22 17.52 22 12 17.52 2 12 2ZM10 17L5 12L6.41 10.59L10 14.17L17.59 6.58L19 8L10 17Z"/>
        
        <!-- Error -->
        <path *ngSwitchCase="'error'" d="M12 2C6.47 2 2 6.47 2 12S6.47 22 12 22 22 17.53 22 12 17.53 2 12 2ZM17 15.59L15.59 17 12 13.41 8.41 17 7 15.59 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59Z"/>
        
      </ng-container>
    </svg>
  `,
  styles: [`
    .icon {
      transition: all 0.2s ease;
    }
    .icon:hover {
      transform: scale(1.1);
    }
    .icon-small { width: 16px; height: 16px; }
    .icon { width: 20px; height: 20px; }
    .icon-large { width: 24px; height: 24px; }
  `]
})
export class IconComponent {
  @Input() name: string = '';
  @Input() size: 'icon-small' | 'icon' | 'icon-large' = 'icon';
  @Input() color?: string;
  @Input() className?: string;
}
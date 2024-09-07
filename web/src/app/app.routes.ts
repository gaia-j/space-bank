import { Routes } from '@angular/router';
import {AuthPageComponent} from "./components/auth-page/auth-page.component";

export const routes: Routes = [
  {path: 'auth', component: AuthPageComponent},
  {path: 'account', loadChildren: () => import('./account/account.module').then(m => m.AccountModule)},
  {path: 'transactions', loadChildren: () => import('./transactions/transactions.module').then(m => m.TransactionsModule)}
];



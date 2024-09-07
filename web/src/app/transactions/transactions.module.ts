import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TransactionsRoutingModule } from './transactions-routing.module';
import {TransactionsComponent} from "./components/transactions/transactions.component";


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    TransactionsRoutingModule,
    TransactionsComponent
  ]
})
export class TransactionsModule { }

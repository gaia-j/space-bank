import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {TransactionsComponent} from "./components/transactions/transactions.component";
import {SendComponent} from "./components/send/send.component";

const routes: Routes = [
  {path: "", component: TransactionsComponent},
  {path: "send", component: SendComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransactionsRoutingModule { }

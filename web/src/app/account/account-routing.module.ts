import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AccountComponent} from "./components/account/account.component";
import {DetailComponent} from "./components/detail/detail.component";

const routes: Routes = [
  {path: "", component: AccountComponent},
  {path: "detail", component: DetailComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountRoutingModule { }

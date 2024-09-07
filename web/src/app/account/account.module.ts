import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountRoutingModule } from './account-routing.module';
import {AccountComponent} from "./components/account/account.component";
import {AngularSvgIconModule, provideAngularSvgIcon, SvgIconComponent} from "angular-svg-icon";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    AccountRoutingModule,
    AccountComponent,
    SvgIconComponent,
    HttpClientModule,
    SvgIconComponent,
  ],
  providers: [
    provideAngularSvgIcon(),
  ]
})
export class AccountModule { }

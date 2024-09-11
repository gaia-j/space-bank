import {Component, inject} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgOptimizedImage} from "@angular/common";
import {SvgIconComponent} from "angular-svg-icon";
import {urls} from "../../../../urls";

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    RouterLinkActive,
    NgOptimizedImage,
    SvgIconComponent
  ],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {

  http = inject(HttpClient);

  ngOnInit(): void {
    this.loadAccount();
  }


  private _taxId: string = '';
  private _name: string = '';
  private _email: string = '';
  private _accountCode: string = '';
  private _balance: number = 0;
  private _birthDate: string = '';

  private _amount = 0;

  get amount(): number {
    return this._amount;
  }

  set amount(value: number) {
    this._amount = value;
  }

  get taxId(): string {
    return this._taxId;
  }

  set taxId(value: string) {
    this._taxId = value;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  get accountCode(): string {
    return this._accountCode;
  }

  set accountCode(value: string) {
    this._accountCode = value;
  }

  get balance(): number {
    return this._balance;
  }

  set balance(value: number) {
    this._balance = value;
  }

  get birthDate(): string {
    return this._birthDate;
  }

  set birthDate(value: string) {
    this._birthDate = value;
  }

  loadAccount(): void {
    this.http.get(urls.account,{
      withCredentials: true
    })
      .subscribe({
      next: (response:any) => {
        this.taxId = response['taxId'];
        this.name = response['name'];
        this.email = response['email'];
        this.accountCode = response['accountCode'];
        this.balance = response['balance'];
        this.birthDate = response['birthDate'];
      },
      error: (error) => {
        console.error(error);
      }
    });
    }

}

import {Component, inject} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {NgIf, NgOptimizedImage} from "@angular/common";
import {CpfMask} from "../../utils/CpfMask";
import {InputHelpers} from "../../utils/InputHelpers";
import {urls} from "../../../urls";

@Component({
  selector: 'app-auth-page',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgOptimizedImage
  ],
  templateUrl: './auth-page.component.html',
  styleUrl: './auth-page.component.css'
})
export class AuthPageComponent {

  private _isLogin: boolean = true;

  private _email: string = '';
  private _password: string = '';

  private _registerName: string = '';
  private _registerEmail: string = '';
  private _registerPassword: string = '';
  private _registerConfirmPassword: string = '';
  private _registerTaxId: string = '';
  private _registerBirthDate: string = '';

  http = inject(HttpClient);

  router = inject(Router);

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  get password(): string {
    return this._password;
  }

  set password(value: string) {
    this._password = value;
  }

  get registerName(): string {
    return this._registerName;
  }

  set registerName(value: string) {
    this._registerName = value;
  }

  get registerEmail(): string {
    return this._registerEmail;
  }

  set registerEmail(value: string) {
    this._registerEmail = value;
  }

  get registerPassword(): string {
    return this._registerPassword;
  }

  set registerPassword(value: string) {
    this._registerPassword = value;
  }

  get registerConfirmPassword(): string {
    return this._registerConfirmPassword;
  }

  set registerConfirmPassword(value: string) {
    this._registerConfirmPassword = value;
  }

  get registerTaxId(): string {
    return this._registerTaxId
  }

  set registerTaxId(value: string) {
    this._registerTaxId = value;
  }

  get registerBirthDate(): string {
    return this._registerBirthDate;
  }

  set registerBirthDate(value: string) {
    this._registerBirthDate = value;
  }

  get isLogin(): boolean {
    return this._isLogin;
  }

  set isLogin(value: boolean) {
    this._isLogin = value
  }

  login(e: Event): void {
    e.preventDefault()
    this.http.post(urls.login, {
      email: this._email,
      password: this._password
    },{ withCredentials: true }).subscribe({
      next: (response) => {
        console.log(response);
        this.router.navigate(['/account']).then(r => console.log(r));
        },
      error: (error) => {
        console.error(error);
        },
      }
    );
  }

  register(e:Event): void {
    e.preventDefault()
    this.http.post(urls.register, {
      name: this._registerName,
      email: this._registerEmail,
      password: this._registerPassword,
      confirmPassword: this._registerConfirmPassword,
      taxId: this._registerTaxId,
      birthDate: this._registerBirthDate
    },{ withCredentials: true }).subscribe({
      next: (response) => {
        this.router.navigate(['/account']).then(r => console.log(r));
        },
      error: (error) => {
        console.error(error);
        },
      }
    );
  }

  change(): void {
    this._isLogin = !this._isLogin;
  }

  protected readonly InputHelpers = InputHelpers;
}


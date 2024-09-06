import {Component, inject} from '@angular/core';
import {HttpClient, provideHttpClient} from "@angular/common/http";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-auth-page',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './auth-page.component.html',
  styleUrl: './auth-page.component.css'
})
export class AuthPageComponent {
  private _email: string = '';
  private _password: string = '';

  private _registerName: string = '';
  private _registerEmail: string = '';
  private _registerPassword: string = '';
  private _registerConfirmPassword: string = '';
  private _registerTaxId: string = '';
  private _registerBirthDate: string = '';

  http = inject(HttpClient);

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
    return this._registerTaxId;
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

  login(e: Event): void {
    e.preventDefault()
    this.http.post('http://localhost:8080/auth/login', {
      email: this._email,
      password: this._password
    }).subscribe({
      next: (response) => {
        console.log(response);
        },
      error: (error) => {
        console.error(error);
        },
      }
    );
  }

  register(e:Event): void {
    e.preventDefault()
    this.http.post('http://localhost:8080/auth/register', {
      name: this._registerName,
      email: this._registerEmail,
      password: this._registerPassword,
      confirmPassword: this._registerConfirmPassword,
      taxId: this._registerTaxId,
      birthDate: this._registerBirthDate
    }).subscribe({
      next: (response) => {
        console.log(response);
        },
      error: (error) => {
        console.error(error);
        },
      }
    );
  }
}


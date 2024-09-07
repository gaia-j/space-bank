import {Component, inject} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {SvgIconComponent} from "angular-svg-icon";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-send',
  standalone: true,
  imports: [
    FormsModule,
    RouterLink,
    RouterLinkActive,
    SvgIconComponent,
    NgIf
  ],
  templateUrl: './send.component.html',
  styleUrl: './send.component.css'
})
export class SendComponent {

  constructor(private router: Router) {
  }

  private _consulted: boolean = false;

  private _destinationAccountCode: string = '';

  private _amount = 0;

  private _searchDestinationAccount: string = '';

  private _destinationTaxId: string = '';

  private _destinationName: string = '';

  get amount(): number {
    return this._amount;
  }

  set amount(value: number) {
    this._amount = value;
  }

  get destinationAccountCode(): string {
    return this._destinationAccountCode;
  }

  set destinationAccountCode(value: string) {
    this._destinationAccountCode = value;
  }

  get searchDestinationAccount(): string {
    return this._searchDestinationAccount;
  }

  set searchDestinationAccount(value: string) {
    this._searchDestinationAccount = value;
  }

  get destinationTaxId(): string {
    return this._destinationTaxId;
  }

  get destinationName(): string {
    return this._destinationName;
  }

  get consulted(): boolean {
    return this._consulted;
  }

  set consulted(value: boolean) {
    this._consulted = value;
  }

  http = inject(HttpClient);

  sendMoney(e: Event): void {
    e.preventDefault();
    this.http.post('http://localhost:8080/transaction/send', {
      destinationAccountCode: this.destinationAccountCode,
      amount: this.amount*100
    }, {
      withCredentials: true
    }).subscribe({
      next: (response) => {
        console.log(response);
        alert('Transferência realizada com sucesso!');
      },
      error: (error) => {
        console.error(error);
        alert('Erro ao realizar transferência!');
      }
    });
  }

  searchAccount(): void{
    this.http.get('http://localhost:8080/account/' + this.searchDestinationAccount, {
      withCredentials: true
    }).subscribe({
      next: (response:any) => {
        this._destinationAccountCode = response.accountCode;
        this._destinationTaxId = response.taxId
        this._destinationName = response.name
        this.consulted = true;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  back(): void {
    this.router.navigate(['/account']).then(r => console.log(r));
  }

}

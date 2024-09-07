import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {TransactionInterface} from "../../../interfaces/transaction.interface";

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgForOf,
    DatePipe
  ],
  templateUrl: './transactions.component.html',
  styleUrl: './transactions.component.css'
})


export class TransactionsComponent {

// {
//   "id": "d3522245-738b-415a-b217-c90b83994d27",
//   "name": "Vinicera",
//   "type": "sent",
//   "time": "2024-09-07T11:48:19.089402",
//   "amount": 30000
// },

  private _transactions: TransactionInterface[] = [];

  get transactions(): TransactionInterface[] {
    return this._transactions;
  }

  set transactions(value: TransactionInterface[]) {
    this._transactions = value;
  }

  constructor(private router: Router, private http: HttpClient) {
  }


  ngOnInit(): void {
    this.loadTransaction();
  }

  loadTransaction(): void {
    this.http.get<TransactionInterface[]>('http://localhost:8080/transaction/list', { withCredentials: true })
      .subscribe({
        next: (response: TransactionInterface[]) => {
          this.transactions = response;
        },
        error: (error) => {
          console.error('Erro ao carregar transações', error);
        }
      });
  }

  back(): void {
    this.router.navigate(['/account']).then(r => console.log(r));
  }

}

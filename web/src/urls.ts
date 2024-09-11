import {environment} from "./environments/environment";

const baseUrl = environment.apiUrl;
export const urls = {
  account: `${baseUrl}/account`,
  login: `${baseUrl}/auth/login`,
  register: `${baseUrl}/auth/register`,
  listTransactions: `${baseUrl}/transaction/list`,
  detailTransaction: `${baseUrl}/transaction`,
  createTransaction: `${baseUrl}/transaction/send`,
}

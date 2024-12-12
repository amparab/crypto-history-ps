export default class HistoryRequest {
    constructor(fromDate, toDate, targetCurrency, offlineMode) {
      this.fromDate = fromDate;
      this.toDate = toDate;
      this.targetCurrency = targetCurrency;
      this.offlineMode = offlineMode;
    }
  }
  
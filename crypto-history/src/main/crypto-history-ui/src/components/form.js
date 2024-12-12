import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import Grid from '@mui/material/Grid2';
import React, { useState, useEffect } from 'react';
import { TextField, Select, MenuItem, Button, InputLabel, FormControl } from '@mui/material';
import HistoryRequest from './../model/historyRequest';
import apiService from './../service/apiService';
import dayjs from 'dayjs';
import Switch from '@mui/material/Switch';
import FormControlLabel from '@mui/material/FormControlLabel';
import { GET_CURRENCY_ENDPOINT, GET_HISTORY_ENDPOINT, SUCCESS_MSG, DEFAULT_CURRENCY_USD } from './../constants/constants';

export default function Form({ displayHistory }) {
  const [fromDate, setFromDate] = useState(null);
  const [toDate, setToDate] = useState(null);
  const [selectedCurrency, setSelectedCurrency] = useState(DEFAULT_CURRENCY_USD);
  const [currencyList, setCurrencyList] = useState(null);
  const [error, setError] = useState(null);
  const [offline, setOffline] = useState(false);

  useEffect(() => {
    getCurrencyList();
  }, []);

  const handleSubmit = () => {
    if(validateInput())
      getHistoryForDateRange();
  };

  const validateInput = () => {
    if(!dayjs(fromDate).isValid() || !dayjs(toDate).isValid() || !selectedCurrency){
      setError("Please enter valid inputs.");
      displayHistory(null);
      return false;
    }   
    return true;
  };


  const getHistoryForDateRange = async () => {
    const request = new HistoryRequest(fromDate.format('YYYY-MM-DD'), toDate.format('YYYY-MM-DD'), selectedCurrency, offline);

    try {
      const result = await apiService.get(GET_HISTORY_ENDPOINT, request);
      if(result.message !== SUCCESS_MSG)
        handleServerError(result.message);
      else
        displayHistory(result);
    } catch (error) {
      console.error('Error making GET request:', error.message);
      handleServerError(error.message);
    }
  };

  const getCurrencyList = async () => {
    try {
      const result = await apiService.get(GET_CURRENCY_ENDPOINT, '');
      setCurrencyList(result);
    } catch (error) {
      console.error('Error making GET request:', error);
    }
  };

  const handleServerError = (error) => {
    console.log(error);
    setError(error);
    displayHistory(null);
  };

  const onFromDateChange = (newDate) => {
    setError(null);
    displayHistory(null);
    setFromDate(newDate);
    setError(null);
  };

  const onToDateChange = (newDate) => {
    setError(null);
    displayHistory(null);
    setToDate(newDate);
    setError(null);
  };

  const onCurrencyChange = (currency) => {
    setError(null);
    displayHistory(null);
    setSelectedCurrency(currency);
    setError(null);
  };

  const handleReset = () => {
    setFromDate(null);
    setToDate(null);
    setSelectedCurrency(DEFAULT_CURRENCY_USD);
    setOffline(false);
    displayHistory(null);
    setError(null);
  };
  
  const handleOfflineModeChange = (event) => {
    console.log(event.target.checked);
    setOffline(event.target.checked);
  };



  return (
    currencyList && <>
      <Grid 
        container 
        spacing={2} 
        justifyContent="center" 
        direction="column"
        alignItems="center"
      >

        <Grid container item spacing={2} justifyContent="center">

          <Grid item>
            <DatePicker
              label="From"
              value={fromDate}
              onChange={(newDate) => onFromDateChange(newDate)}
              renderInput={(params) => <TextField {...params} />}
            />
          </Grid>

          <Grid item>
            <DatePicker
              label="To"
              value={toDate}
              onChange={(newDate) => {onToDateChange(newDate)}}
              renderInput={(params) => <TextField {...params} />}
            />
          </Grid>
        </Grid>

        <Grid item xs={12} style={{ display: 'flex', justifyContent: 'center', width: '100%' }}>
          <FormControl style={{ width: '50%' }}>
            <InputLabel id="dropdown-label">Select Option</InputLabel>
            <Select
              labelId="dropdown-label"
              value={selectedCurrency}
              onChange={(e) => onCurrencyChange(e.target.value)}
              label="Select Option"
              fullWidth
            >
              {
                currencyList.map((currency, index) => (
                  <MenuItem key={index} value={currency}>
                    {currency}
                  </MenuItem>
                ))
              }
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs={12} container spacing={2} justifyContent="center" alignItems="center">
          <Button 
            variant="contained" 
            color="primary" 
            onClick={handleSubmit}
          >
            Submit
          </Button>
          <Button 
          variant="outlined" 
          onClick={handleReset}
          style={{ marginRight: '10px' }}
        >
          Reset
        </Button>
          <FormControlLabel control={<Switch onChange={handleOfflineModeChange} />} label="Offline Mode" />
        </Grid>
      </Grid>
      {error && <h4>{error}</h4>}
    </>
  );
}

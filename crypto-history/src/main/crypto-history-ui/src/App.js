import Form from './components/form';
import Result from './components/result';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid2';
import React, { useState } from 'react';

const App = () => {

  const [history, setHistory] = useState(null);

  const displayHistory = (data) => {
    setHistory(data);
  };

  
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Box 
        display="flex" 
        justifyContent="center" 
        alignItems="center" 
        height="100vh"
        sx={{ backgroundColor: '#f0f0f0' }}
      >
        <Grid container spacing={2} direction="column" alignItems="center">
          {/* Title */}
          <Grid item xs={12}>
            <h2>
              HISTORICAL BITCOIN
            </h2>
          </Grid>

          <Form displayHistory={displayHistory} />
          <Result historyData={history} />
        </Grid>
      </Box>
    </LocalizationProvider>
    
  );
}

export default App;

import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import React from 'react';

export default function Result({historyData}) {

  return (
    historyData && <Box sx={{ padding: 1 }}>
      <TableContainer
        component={Paper}
        elevation={3}
        sx={{ maxHeight: 400, overflow: 'auto', padding: 0, marginTop: 2 }}
      >
        <Table>
          <TableHead>
            <TableRow>
              <TableCell align="center" sx={{ padding: '8px' }}><strong>Date</strong></TableCell>
              <TableCell align="center" sx={{ padding: '8px' }}><strong>USD Value</strong></TableCell>
              <TableCell align="center" sx={{ padding: '8px' }}><strong>
                {historyData.targetCurrency} Value</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {
              historyData.priceHistory.map((row, index) => (
                <TableRow key={index}>
                  <TableCell align="center" sx={{ padding: '8px' }}>{row.date}</TableCell>
                  <TableCell align="center" sx={{ padding: '8px' }}>{row.usdValue}</TableCell>
                  <TableCell align="center" sx={{ padding: '8px' }}>{row.targetValue}</TableCell>
                </TableRow>
              ))
            }
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}



import { BASE_URL } from './../constants/constants';
const apiService = {

    get: async (endpoint, params) => {
      try {
        const queryString = new URLSearchParams(params).toString();
        const url = queryString ? `${BASE_URL}${endpoint}?${queryString}` : `${BASE_URL}${endpoint}`;

        const response = await fetch(url, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });
       
        if (!response.ok) {
          throw new Error(response.message);
        }
        return await response.json();
      } catch (error) {
        console.error('Error in GET request:', error);
        throw error;
      }
    }
  };
  
  export default apiService;
  
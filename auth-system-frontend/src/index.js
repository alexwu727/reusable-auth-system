import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './app/App';
import store from './app/store';
import theme from './app/theme';

import { Provider } from 'react-redux';
import { ThemeProvider } from '@mui/material/styles';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <ThemeProvider theme={theme}>
        <App />
      </ThemeProvider>
    </Provider>
  </React.StrictMode>
);


import { createTheme } from "@mui/material/styles";

export const theme = createTheme({
  palette: {
    primary: {
      main: "#005EA8", 
      dark: "#00447B",
    },
    background: {
      default: "#F5F7FA", 
    },
    text: {
      primary: "#333333",
      secondary: "#666666",
    },
    error: {
      main: "#B00020",
    },
    success: {
      main: "#28A745",
    },
    warning: {
      main: "#FFC107",
    }
  },
  shape: {
    borderRadius: 12,
  }
});

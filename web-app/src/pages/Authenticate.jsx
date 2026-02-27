import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { setToken } from "../services/localStorageService";
import { Box, CircularProgress, Typography } from "@mui/material";

export default function Authenticate() {
  const navigate = useNavigate();
  const [isLoggedin, setIsLoggedin] = useState(false);

  useEffect(() => {
    const codeRegex = /code=([^&]+)/;
    const isMatch = window.location.href.match(codeRegex);

    if (isMatch) {
      const code = isMatch[1];

      fetch("http://localhost:9000/api/v1/identity/auth/outbound/authenticate?code=" + code, {
        method: "POST"
      }).then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("Authentication failed");
        }
      }).then((data) => {
        setToken(data.data.token);
        setIsLoggedin(true);
      }).catch((error) => {
        console.error("Error during authentication:", error);
      });
    }
  }, []);

  useEffect(() => {
    if (isLoggedin) {
      navigate("/");
    }
  }, [isLoggedin, navigate]);

  return (
    <>
      <Box
        sx={{
          display: "flex",
          flexDirection : "column",
          gap: "30px",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <CircularProgress></CircularProgress>
        <Typography>Authenticating...</Typography>
      </Box>
    </>
  );
}
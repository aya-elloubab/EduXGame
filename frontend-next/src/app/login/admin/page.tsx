"use client";

import React, { useState } from "react";
import {
  Box,
  Button,
  Container,
  CssBaseline,
  TextField,
  Typography,
  Link,
} from "@mui/material";
import { useRouter } from "next/navigation";

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    setMessage("Login successful!");
    
    // Simulate redirect delay
    setTimeout(() => {
      router.push("/dashboard");
    }, 1000);
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        backgroundImage: 'url("/images/bg.jpg")',
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundAttachment: "fixed",
        backgroundBlendMode: "overlay",
        backgroundColor: "rgba(0, 0, 0, 0.5)",
      }}
    >
      <CssBaseline />
      <Container
        maxWidth="xs"
        sx={{
          backgroundColor: "rgba(255, 255, 255, 0.85)",
          borderRadius: 4,
          boxShadow: "0px 8px 15px rgba(0, 0, 0, 0.2)",
          padding: 4,
        }}
      >
        <Typography
          component="h1"
          variant="h4"
          align="center"
          gutterBottom
          sx={{
            fontWeight: "bold",
            backgroundImage: "linear-gradient(to right, #6366F1, #8B5CF6)",
            color: "transparent",
            backgroundClip: "text",
          }}
        >
          Admin Login
        </Typography>
        <Typography
          variant="body2"
          align="center"
          sx={{ color: "text.secondary", mb: 3 }}
        >
          Sign in to manage your application
        </Typography>

        <Box
          component="form"
          noValidate
          onSubmit={handleSubmit}
          sx={{
            display: "flex",
            flexDirection: "column",
            gap: 2,
          }}
        >
          {message && (
            <Typography
              color="success"
              variant="body2"
              align="center"
              sx={{ mb: 2 }}
            >
              {message}
            </Typography>
          )}
          <TextField
            id="email"
            label="Email"
            type="email"
            variant="outlined"
            fullWidth
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <TextField
            id="password"
            label="Password"
            type="password"
            variant="outlined"
            fullWidth
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{
              textTransform: "none",
              fontSize: "16px",
              py: 1.5,
              backgroundImage: "linear-gradient(to right, #6366F1, #8B5CF6)",
              boxShadow: "0px 4px 10px rgba(99, 102, 241, 0.3)",
            }}
          >
            Login
          </Button>
        </Box>

        <Typography
          variant="body2"
          align="center"
          sx={{ mt: 2, color: "text.secondary" }}
        >
          Forgot your password?{" "}
          <Link href="#" underline="hover" color="primary">
            Reset it
          </Link>
        </Typography>
      </Container>
    </Box>
  );
}

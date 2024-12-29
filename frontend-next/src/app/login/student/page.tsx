'use client'
import React from 'react';
import {
Box,
Button,
Container,
CssBaseline,
TextField,
Typography,
Link,
} from '@mui/material';

export default function LoginPage() {
return (
    <Box
    sx={{
        minHeight: '100vh',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundImage: 'url("/images/bg.jpg")',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
    }}
    >
    <CssBaseline />
    <Container
        maxWidth="xs"
        sx={{
        backgroundColor: 'rgba(255, 255, 255, 0.9)', // White with slight transparency
        borderRadius: 2,
        boxShadow: 3,
        padding: 4,
        }}
    >
        {/* Header */}
        <Typography
        component="h1"
        variant="h5"
        align="center"
        gutterBottom
        sx={{ fontWeight: 'bold', color: 'text.primary' }}
        >
        Welcome back 
        </Typography>
        <Typography
        variant="body2"
        align="center"
        sx={{ color: 'text.secondary', mb: 3 }}
        >
        Sign in to continue your journey
        </Typography>

        {/* Login Form */}
        <Box
        component="form"
        noValidate
        sx={{
            display: 'flex',
            flexDirection: 'column',
            gap: 2,
        }}
        >
        {/* Email Input */}
        <TextField
            id="email"
            label="Email"
            type="email"
            variant="outlined"
            fullWidth
            required
        />
        {/* Password Input */}
        <TextField
            id="password"
            label="Password"
            type="password"
            variant="outlined"
            fullWidth
            required
        />
        {/* Submit Button */}
        <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{
            textTransform: 'none',
            fontSize: '16px',
            py: 1.5,
            }}
        >
            Login
        </Button>
        </Box>

        {/* Footer */}
        <Typography
        variant="body2"
        align="center"
        sx={{ mt: 2, color: 'text.secondary' }}
        >
        Forgot your password?{' '}
        <Link href="#" underline="hover" color="primary">
            Reset it
        </Link>
        </Typography>
    </Container>
    </Box>
);
}

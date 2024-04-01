import React from 'react'
import { Button } from '@mui/material';

const FluorescentButton = ({ children, color, style }) => {
    return (
        <Button
            sx={{
                position: 'relative',
                overflow: 'hidden',
                transition: 'all 0.3s',
                '&:before': {
                    content: '""',
                    position: 'absolute',
                    bottom: '15%',
                    left: '1%',
                    width: '90%',
                    height: '30%', // Cover only the bottom half
                    // backgroundImage: `linear-gradient(to right, ${color || '#f44336'}, ${color || '#3f51b5'})`,
                    background: color || '#f44336bb', // Default color is '#f44336bb'
                    transform: 'scaleX(0)',
                    transition: 'transform 0.3s',
                    transformOrigin: 'left', // Start the transform from the left
                },
                '&:hover': {
                    background: 'transparent',
                },
                '&:hover:before': {
                    transform: 'scaleX(1)', // Expand the pseudo-element to full width on hover
                },
                ...style
            }}
        >
            {children}
        </Button>
    );
};

export default FluorescentButton
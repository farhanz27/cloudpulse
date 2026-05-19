import { definePreset } from '@primevue/themes'
import Aura from '@primevue/themes/aura'

const CloudPulsePreset = definePreset(Aura, {
  semantic: {
    primary: {
      50: '#fefce8',
      100: '#fef9c3',
      200: '#fde68a',
      300: '#fcd34d',
      400: '#e8c84a',
      500: '#D4AF37',
      600: '#b8952f',
      700: '#927a26',
      800: '#715e1d',
      900: '#4e4115',
      950: '#2a220b',
    },
    colorScheme: {
      dark: {
        surface: {
          0: '#F2F0E4',
          50: '#dedad0',
          100: '#c4c0b3',
          200: '#9e9a8d',
          300: '#787467',
          400: '#524e41',
          500: '#2c281b',
          600: '#222018',
          700: '#1a1816',
          800: '#141414',
          900: '#0f0f0f',
          950: '#0A0A0A',
        },
        primary: {
          color: '{primary.500}',
          contrastColor: '#0A0A0A',
          hoverColor: '{primary.400}',
          activeColor: '{primary.600}',
        },
        highlight: {
          background: 'rgba(212,175,55,0.15)',
          focusBackground: 'rgba(212,175,55,0.2)',
          color: '#D4AF37',
          focusColor: '#D4AF37',
        },
      },
    },
  },
})

export default CloudPulsePreset

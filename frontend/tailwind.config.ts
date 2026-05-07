import type { Config } from "tailwindcss";

const config: Config = {
  content: ["./app/**/*.{ts,tsx}", "./components/**/*.{ts,tsx}", "./lib/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        brand: {
          50: "#fff7ed",
          100: "#ffedd5",
          500: "#f97316",
          600: "#ea580c",
          700: "#c2410c",
        },
        ink: "#1f2937",
        mist: "#f5f6f8",
      },
      boxShadow: {
        soft: "0 12px 34px rgba(15, 23, 42, 0.08)",
      },
    },
  },
  plugins: [],
};

export default config;

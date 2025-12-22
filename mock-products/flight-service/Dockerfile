FROM node:20-alpine
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install --production

# Copy all source files (adjust based on actual structure)
COPY . .

ENV PORT=9001
EXPOSE 9001

# Adjust this to match your actual entry point
CMD ["node", "index.js"]
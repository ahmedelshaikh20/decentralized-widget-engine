FROM node:20-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install --production

# Copy source code
COPY . .

ENV PORT=9001
EXPOSE 9001

CMD ["node", "src/server.js"]

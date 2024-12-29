# Use the official Node.js 18 image from the Docker Hub
FROM node:18

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json and package-lock.json (or yarn.lock) to the working directory
COPY package*.json ./

# Install the dependencies
RUN npm install

# Copy the rest of the application code into the container
COPY . .

# Build the Next.js app (optional if you want to run a production build)
RUN npm run build

# Expose the port that your Next.js app will run on
EXPOSE 3000

# Set the environment variable to production (optional)
ENV NODE_ENV=production

# Start the Next.js app
CMD ["npm", "start"]
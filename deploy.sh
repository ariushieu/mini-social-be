#!/bin/bash

# Deploy script for VPS
# This script should be placed on the VPS at /opt/mini-social-be/deploy.sh

set -e

echo "Starting deployment..."

# Navigate to project directory
cd /opt/mini-social-be

# Pull latest Docker image
echo "Pulling latest Docker image..."
docker-compose pull app

# Stop and remove old container
echo "Stopping old container..."
docker-compose down app

# Start new container
echo "Starting new container..."
docker-compose up -d app

# Clean up old images
echo "Cleaning up old images..."
docker image prune -f

echo "Deployment complete!"
echo "Container status:"
docker-compose ps

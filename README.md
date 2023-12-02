<a name="readme-top"></a>

# CareTaker App

A caregiving Android App.

## Table of Contents

1. [Prerequisites](#prerequisites)

    - <a href="#prerequisite-a">A. Set up API Keys</a>

## Prerequisites

Here are the steps to set up your environment:

### A. Set up API Keys <a name="prerequisite-a"></a>

1. Set up API Keys

   Please set up the following 3 API KEYS in gradle.properties:
   ```bash
   # Obtained from (https://console.cloud.google.com/apis/)
   GOOGLE_MAPS_API_KEY=YOUR_API_KEY
   # Obtained from (https://account.mapbox.com/)
   MAPBOX_ACCESS_TOKEN=YOUR_API_KEY
   MAPBOX_DOWNLOADS_TOKEN=YOUR_API_KEY
   ```

2. Set up Server IP

   Please set up the backend server IP in gradle.properties:
   ```bash
   # Base IP of server backend
   SERVER_IP=http://10.70.21.92:5000
   ```
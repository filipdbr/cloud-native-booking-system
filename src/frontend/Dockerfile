# choosing the latest light version for today
FROM nginx:stable-alpine3.23

# remove nginx default html files
RUN rm -rf /usr/share/nginx/html/*

# copy local html files to the container
COPY html /usr/share/nginx/html

# copyt local config file to the container
# order is important! nginx.conf changes more often then html files
COPY nginx.conf /etc/nginx/nginx.conf

# we need to prevent docker from using root user
# change ownership of the catalogs to 'nginx' user & group
RUN chown -R nginx:nginx /usr/share/nginx/html \
                         /var/cache/nginx \
                         /var/log/nginx && \
    touch /var/run/nginx.pid && \
    chown nginx:nginx /var/run/nginx.pid

# switch the execution context from 'root' to the limited 'nginx' user to enforce the principle of least privilege
USER nginx

# inform Docker that the container listens on port 80 at runtime
EXPOSE 80

# run Nginx in the foreground to keep the container active and continuously stream logs to the stdout
CMD ["nginx", "-g", "daemon off;"]
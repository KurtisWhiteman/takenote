FROM mysql:5.7

# Copy a local version of my.cnf to configure mysql db to match aurora
COPY ./docker/database/my.cnf /etc/mysql/conf.d/my.cnf
RUN chmod 644 /etc/mysql/conf.d/my.cnf

# Set timezone
RUN rm -rf /etc/localtime && ln -s /usr/share/zoneinfo/Australia/Sydney /etc/localtime

CMD ["mysqld"]

EXPOSE 3306

#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>

int main() {
    char str[100];
    int listen_fd;
    int comm1_fd, comm2_fd;

    struct sockaddr_in servaddr;

    listen_fd = socket(AF_INET, SOCK_STREAM, 0);
    bzero(&servaddr, sizeof(servaddr));

    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htons(INADDR_ANY);
    servaddr.sin_port = htons(9999);

    bind(listen_fd, (struct sockaddr *) &servaddr, sizeof(servaddr));
    listen(listen_fd, 10);

    comm1_fd = accept(listen_fd, (struct sockaddr*) NULL, NULL);
    printf("First connection OK - %d\n", comm1_fd);
    comm2_fd = accept(listen_fd, (struct sockaddr*) NULL, NULL);
    printf("Second connection OK - %d\n", comm2_fd);

    while (1) {
        bzero(str, 100);
        read(comm1_fd, str, 100);
        if (strlen(str) > 0) {
            printf("Forwarding - %s", str);
            write(comm2_fd, str, strlen(str));
        }
    }

    return 0;
}

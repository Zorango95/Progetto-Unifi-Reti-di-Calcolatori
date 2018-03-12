# Mattia D'Autila

"""
Checks the implemented functions of the HTTP / 1.0 protocol by sending the same client of GET, HEAD, POST.
"""

import socket

# Sets the host values with the server's localhost IP and the port on which the server accepts client connections.
myHost = "localhost"
myPort = 12000

# Create and connect the client to the myHost Server on the myPort port.
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((myHost, myPort))
print("Connection client: ")
print(sock.getsockname())
raw_input("\nPress enter to send the first request\n")

"""
Manages the sending of the request to the server, sending also the string of request "stop".
Manages reception of the reply from the server.
"""
# @param request: indicates the request to send to the server
def comunication(request):

    sock.send(request)
    stop = "stop\n"
    sock.send(stop)
    print("Request send to server :\n\n" + request)

    reply = sock.recv(2048)
    print("Reply receipt from server :\n\n" + reply)

# Send a GET request with HTTP/1.0 version with the correct Host. It is managed with handlerHost1_1.
print("Request GET HTTP/1.0; Host correct: MIO.PC; Handler: HandlerHost1_1; Path: Ok\n")
request = "GET ./TestGetRequest.html HTTP/1.0\r\nHost: Mio.P\r\nConnection: Keep-alive\r\n\r\n"
comunication(request)

raw_input("\nPress enter to continue for next request\n")

# Send a HEAD request with HTTP/1.0 version with the correct Host. It is managed with handlerHost1_0.
print("Request HEAD HTTP/1.0; Host correct: MIO.PC; Handler: HandlerHost1_0; Path: Ok\n")
request = "HEAD /TestHeadRequest.html HTTP/1.0\r\nHost: Mio.PC\r\nConnection: Keep-alive\r\n\r\n"
comunication(request)

raw_input("\nPress enter to continue for next request\n")

# Send a POST request with HTTP/1.0 version without the correct Host. It is managed with handler1_0.
print("Request POST HTTP/1.0; Host not correct: Mio.Pc; Handler: Handler1_0; Path: Ok\n")
request = "POST /TestPostRequest.html HTTP/1.0\r\nHost: Mio.Pc\r\nConnection: Close\r\n\r\nOK\n"
comunication(request)

print("\nFinish request")

# Closes the InputStream, OutputStream and the client connection.
print("\nConnection closed: ")
print(sock.getsockname())
sock.close()

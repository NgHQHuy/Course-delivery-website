1. Đối với ứng dụng giao diện (React):
	- Truy cập thư mục ứng dụng (thư mục FE)
	- Mở termianl
	- Thực hiện lệnh "npm install" hoặc "npm i" để tiến hành cài dặt cái package
	- Sau khi cài đặt đầy đủ package, khởi ứng dụng bằng lệnh "npm start"

2. Đối với các service ở Back-end:
	a. Yêu cầu:
	- Java 17
	- Docker
	b. Chạy các service:
	- Truy cập vào thư mục "BE":
	+ Nếu là Windows, chạy lệnh ./mvnw.cmd clean package -DskipTests để biên dịch chương trình.
	+ Nếu là Linux, chạy lệnh ./mvnw clean package -DskipTests để biên dịch chương trình.
	- Chạy lệnh docker compose up -d hoặc docker-compose up -d nếu đang xài phiên bản cũ, để chạy các service lên.

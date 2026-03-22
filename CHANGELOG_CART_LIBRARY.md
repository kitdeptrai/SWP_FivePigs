# Cart + Library Integration Summary

## 1) Files Added

### [src/main/java/com/fivepigs/app/dao/CartDao.java]
- Vai tro:
  - Xu ly nghiep vu gio hang va thanh toan.
- Chuc nang chinh:
  - Lay danh sach item trong cart theo `customer_id`.
  - Them san pham vao cart (co check trung item va check da co license).
  - Xoa san pham khoi cart.
  - Checkout theo transaction:
    - Tao `orders`
    - Tao `order_detail`
    - Tao `license` (de dua vao Library)
    - Xoa `cart_detail` sau khi thanh toan.
  - Tinh tong tien cart.

### [src/main/java/com/fivepigs/app/web/customer/CartServlet.java]
- Vai tro:
  - Endpoint `/cart` cho customer.
- Chuc nang chinh:
  - `GET /cart`: Hien thi gio hang.
  - `POST /cart?action=add`: Them item vao gio.
  - `POST /cart?action=remove`: Xoa item khoi gio.
  - `POST /cart?action=checkout`: Thanh toan, tao order + license, redirect sang Library.
  - Kiem tra session user; neu chua login thi redirect `/login?redirect=/cart`.

### [src/main/webapp/WEB-INF/views/customer/cart.jsp]
- Vai tro:
  - Giao dien Cart.
- Chuc nang chinh:
  - Hien thi danh sach item trong gio.
  - Hien thi tong so item + tong tien.
  - Nut `Checkout`.
  - Nut xoa item tung dong.
  - Hien thong bao (`added`, `exists`, `removed`, `empty`).

---

## 2) Files Modified

### [src/main/java/com/fivepigs/app/dao/SoftwareDao.java]
- Sua method `getLibraryByUserIdWithIcon(int userId)`.
- Thay vi doc tu bang `user_library` (khong ton tai), da doi sang doc tu `license` + `software` + `software_image`.
- Ket qua:
  - Library du lieu dung theo schema hien tai.
  - Khong con loi SQL `Table 'fivepigs.user_library' doesn't exist`.

### [src/main/java/com/fivepigs/app/web/customer/LibraryServlet.java]
- Sua logic lay user session:
  - Uu tien `session.user.userId`.
  - Fallback `session.userId` neu can.
- Neu chua login: redirect `/login?redirect=/library`.
- Muc tieu:
  - Tranh null session key.
  - Sau login quay lai dung trang Library.

### [src/main/java/com/fivepigs/app/web/LoginServlet.java]
- Them ho tro tham so `redirect`:
  - Nhap login tu `/login?redirect=/library` hoac `/cart`.
  - Login thanh cong thi quay lai URL do thay vi dashboard mac dinh.
- Co sanitize `redirect` de tranh open redirect.

### [src/main/webapp/WEB-INF/views/login.jsp]
- Them hidden input `redirect` trong form login.
- Muc tieu:
  - Giu duoc trang dich sau khi submit login.

### [src/main/webapp/WEB-INF/views/customer/header.jsp]
- Doi UI user goc phai:
  - Neu chua login: hien nut `Login`.
  - Neu da login: hien avatar + ten + dropdown logout.
- Muc tieu:
  - Biet ro trang thai dang nhap that (khong con icon gia lap de gay nham).

### [src/main/webapp/WEB-INF/views/customer/sidebar.jsp]
- Them menu `Cart` vao sidebar.
- Muc tieu:
  - Cho user truy cap gio hang nhanh.

### [src/main/webapp/WEB-INF/views/customer/single-product.jsp]
- Doi cum nut thao tac:
  - Tu nut `Install/Wishlist/Share` gia lap
  - Sang `Add To Cart` + `Go To Cart`.
- Muc tieu:
  - Noi thang vao flow mua hang Cart -> Checkout -> Library.

---

## 3) Business Flow Sau Khi Tich Hop

1. User vao trang chi tiet san pham (`/product?pid=...`).
2. Bam `Add To Cart` -> `POST /cart?action=add`.
3. Vao `/cart` de xem gio.
4. Bam `Checkout` -> tao `orders` + `order_detail` + `license`.
5. Redirect sang `/library`.
6. Library doc tu bang `license`, nen app vua mua se hien trong `My Library`.

---

## 4) Ly Do Truoc Day Bi Loi

- Sidebar `Library` van click duoc, nhung backend:
  - Doc session key khong khop (de gay redirect login/home).
  - Quan trong hon: query `user_library` trong khi schema DB khong co bang nay.
- Da xu ly ca 2 van de:
  - Dong bo session login.
  - Chuyen datasource Library sang `license`.

---

## 5) Ghi Chu Van Hanh

- De test day du flow, can login bang account customer.
- Cac endpoint Cart/Library deu yeu cau session user hop le.
- Checkout hien tai dang mo phong thanh toan thanh cong ngay (tao `PAID` neu chua co).

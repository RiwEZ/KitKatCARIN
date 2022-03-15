<h1 align="">Covid Antibody Response and Infection Neutralization</h1>
<br>
<p align="">
  <a href="https://github.com/RiwEZ/KitKatCARIN/">
    <img alt="CARIN" title="CARIN" src="https://github.com/0736b/0736b/blob/main/LOGO.jpg">
  </a>
</p>
<h4 align="">A simulation game where the player has to bring antibodies to battle viruses. Both of them act according to a specific genetic code. Genetic code can be determined using a specific grammar.</h4>
<br>

## 👶🏻 Developer (KitKat Group)
* Tanat Tangun
* Thanawat Bumpengpun
* Tananun Chowdee
## 📚 Library 
* <a href="https://github.com/gurkenlabs/litiengine"> LITIENGINE </a> Java 2D Game Engine
## 🔧 Tool
* IntelliJ IDEA
* Gradle Build Tool
* JDK 17
## 📄 How-To-Play
* #### เป้าหมาย
  - หากกำจัด Virus ในพื้นที่ได้หมดทุกตัว (ชนะ)
  - หาก Antibody ที่ผู้เล่นมีตายหมด (แพ้)

* #### การเล่น
  - มีพื้นที่ขนาด m x n ที่ Antibody และ Virus สามารถอยู่ได้
  - เมื่อเริ่มเกมจะมี Virus สุ่มเกิดขึ้นมา 3 ตัว และสุ่มเกิดเรื่อยๆตลอดทั้งเกม
  - เมื่อเริ่มเกมผู้เล่นจะต้องใช้ Credit ซื้อ Antibody เพื่อนำมาต่อสู้กับ Virus โดยการคลิกและลากมาวางตรงพื้นที่ที่ต้องการ (เลือก Genetic code ได้)
  - หาก Antibody กำจัด Virus ได้ จะได้รับ Credit เพื่อนำไปซื้อ Antibody เพิ่มเติมได้
  - หาก Antibody ถูก Virus โจมตีตาย Antibody ตัวนั้นจะกลายเป็น Virus
  - มีจำนวน Antibody และ Virus ที่อยู่ในพื้นที่บอก

* #### การควบคุม
   - ควบคุมกล้อง
     - เคลื่อนย้ายกล้อง | กด W, A, S, D
     - ซูมเข้า-ออก | กด Ctrl + ลูกกลิ้งเมาส์ขึ้น-ลง
     - รีเซ็ตกล้อง | ให้ซูมออกจนสุด
    
   - ความเร็วเกม
     - เร่ง-ลดความเร็วเกม | ใช้เมาส์คลิกเลื่อนแถบ Speed Slider
     - หยุดเกม | กด Spacebar หรือ คลิกที่ปุ่ม (เกมจะหยุดเองเมื่อผู้เล่นทำการซื้อหรือเคลื่อนย้าย Antibody ด้วยตัวเอง)

* #### การตั้งค่า
  - แก้ไขในไฟล์ gameconfig.properties
    - ขนาดพื้นที่ m x n
    - อัตราการเกิดของ Virus
    - Credit เริ่มต้น
    - ราคา Antibody
    - เลือดของ Antibody, Virus
    - พลังโจมตีของ Antibody, Virus
    - เลือดที่ต้องเสียเมื่อเคลื่อนย้าย Antibody ด้วยตนเอง
    - Credit ที่จะได้รับเมื่อ Antibody กำจัด Virus ได้
   
   - แก้ไขไฟล์ในโฟลเดอร์ genetic_codes
     - แก้ไขโค้ดในไฟล์ .in ในโฟลเดอร์ Antibody เพื่อกำหนดการกระทำของ Antibody
     - แก้ไขโค้ดในไฟล์ .in ในโฟลเดอร์ Virus เพื่อกำหนดการกระทำของ Virus
  <br><br>
  <hr>
  <h4 align="center">261200 | Object-Oriented Programming</h4>

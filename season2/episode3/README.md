### Дедлок на 146%

Класс [Deadlock](https://github.com/TimurTechnopolis/advanced_java_course_2017/tree/master/season2/episode3/src/main/java/edu/technopolis/advancedjava/Deadlock.java)
является шаблоном для написания приложения, запуск которого на любой платформе, поддерживающей java (даже на процессоре 
с одним ядром, где потоки по сути работают последовательно), должен приводить к гарантированному 
[Дедлоку](https://docs.oracle.com/javase/tutorial/essential/concurrency/deadlock.html).

Чтобы убедиться, что дедлок достингут, нужно сделать threaddump c помощью утилиты `jstack` или нажав в IDEA кнопку 
во вкладке Run (иконка фотоаппарат) "Dump threads".
При этом будет в дампе должно быть указано
```
Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x00007fea66062e00 (object 0x00000006cfda1168, a java.lang.Object),
  which is held by "Thread-0"
"Thread-0":
  waiting to lock monitor 0x00007fea66060f00 (object 0x00000006cfda1178, a java.lang.Object),
  which is held by "Thread-1"


Found 1 deadlock.
```
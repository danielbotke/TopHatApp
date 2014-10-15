/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mineracao;

import dao.DeviceDao;
import dao.HistActionDao;
import dao.ToDoActionDao;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import models.Device;
import models.HistAction;
import models.Home;
import models.IAction;
import models.ToDoAction;

/**
 *
 * @author Daniel
 */
public class Mineracao {

    public void miningHistory(Home h) {
        HistActionDao histDao = new HistActionDao();
        ToDoActionDao todoDao = new ToDoActionDao();
        DeviceDao deviceDao = new DeviceDao();
        Date date = new Date();
        if (date.getMonth() > 3) {
            date.setMonth(date.getMonth() - 3);
        } else if (date.getMonth() == 3) {
            date.setMonth(12);
            date.setYear(date.getYear() -1);
        } else if (date.getMonth() == 2) {
            date.setMonth(11);
            date.setYear(date.getYear() -1);
        } else if (date.getMonth() == 1) {
            date.setMonth(10);
            date.setYear(date.getYear() -1);
        }
        String actions[] = {"l", "d", "turOff", "turOn"};
        List<Device> devices = deviceDao.listAll();
        for (int d = 0; d < devices.size(); d++) {
            for (int a = 0; a < actions.length; a++) {
                List<HistAction> data = histDao.listPeriod(date, new Date(), actions[a], devices.get(d));
                List<HistAction> weekend = new ArrayList<>();
                List<HistAction> weekdays = new ArrayList<>();
                List<HistAction> weekendAfternoon = new ArrayList<>();
                List<HistAction> weekendMorning = new ArrayList<>();
                List<HistAction> weekdaysAfternoon = new ArrayList<>();
                List<HistAction> weekdaysMorning = new ArrayList<>();
                int[] minutes;
                minutes = new int[48];
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getDateTime().getDay() == 1 || data.get(i).getDateTime().getDay() == 6) {
                        weekend.add(data.get(i));
                    } else {
                        weekdays.add(data.get(i));
                    }
                }
                for (int i = 0; i < weekdays.size(); i++) {
                    if (weekdays.get(i).getDateTime().getHours() < 12) {
                        weekdaysMorning.add(weekdays.get(i));
                    } else {
                        weekdaysAfternoon.add(weekdays.get(i));
                    }

                }
                for (int i = 0; i < weekend.size(); i++) {
                    if (weekend.get(i).getDateTime().getHours() < 12) {
                        weekendMorning.add(weekend.get(i));
                    } else {
                        weekendAfternoon.add(weekend.get(i));
                    }

                }
                Iterator it = weekendMorning.iterator();
                HistAction aux;
                int hour;
                int minute;
                while (it.hasNext()) {
                    aux = (HistAction) it.next();
                    hour = aux.getDateTime().getHours();
                    minute = aux.getDateTime().getMinutes();
                    switch (hour) {
                        case 0:
                            if (minute < 30) {
                                minutes[0]++;
                            } else {
                                minutes[1]++;
                            }
                            break;
                        case 1:
                            if (minute < 30) {
                                minutes[2]++;
                            } else {
                                minutes[3]++;
                            }
                            break;
                        case 2:
                            if (minute < 30) {
                                minutes[4]++;
                            } else {
                                minutes[5]++;
                            }
                            break;
                        case 3:
                            if (minute < 30) {
                                minutes[6]++;
                            } else {
                                minutes[7]++;
                            }
                            break;
                        case 4:
                            if (minute < 30) {
                                minutes[8]++;
                            } else {
                                minutes[9]++;
                            }
                            break;
                        case 5:
                            if (minute < 30) {
                                minutes[10]++;
                            } else {
                                minutes[11]++;
                            }
                            break;
                        case 6:
                            if (minute < 30) {
                                minutes[12]++;
                            } else {
                                minutes[13]++;
                            }
                            break;
                        case 7:
                            if (minute < 30) {
                                minutes[14]++;
                            } else {
                                minutes[15]++;
                            }
                            break;
                        case 8:
                            if (minute < 30) {
                                minutes[16]++;
                            } else {
                                minutes[17]++;
                            }
                            break;
                        case 9:
                            if (minute < 30) {
                                minutes[18]++;
                            } else {
                                minutes[19]++;
                            }
                            break;
                        case 10:
                            if (minute < 30) {
                                minutes[20]++;
                            } else {
                                minutes[21]++;
                            }
                            break;
                        case 11:
                            if (minute < 30) {
                                minutes[22]++;
                            } else {
                                minutes[23]++;
                            }
                            break;
                    }
                }
                it = weekendAfternoon.iterator();
                while (it.hasNext()) {
                    aux = (HistAction) it.next();
                    hour = aux.getDateTime().getHours();
                    minute = aux.getDateTime().getMinutes();
                    switch (hour) {
                        case 12:
                            if (minute < 30) {
                                minutes[24]++;
                            } else {
                                minutes[25]++;
                            }
                            break;
                        case 13:
                            if (minute < 30) {
                                minutes[26]++;
                            } else {
                                minutes[27]++;
                            }
                            break;
                        case 14:
                            if (minute < 30) {
                                minutes[28]++;
                            } else {
                                minutes[29]++;
                            }
                            break;
                        case 15:
                            if (minute < 30) {
                                minutes[30]++;
                            } else {
                                minutes[31]++;
                            }
                            break;
                        case 16:
                            if (minute < 30) {
                                minutes[32]++;
                            } else {
                                minutes[33]++;
                            }
                            break;
                        case 17:
                            if (minute < 30) {
                                minutes[34]++;
                            } else {
                                minutes[35]++;
                            }
                            break;
                        case 18:
                            if (minute < 30) {
                                minutes[36]++;
                            } else {
                                minutes[37]++;
                            }
                            break;
                        case 19:
                            if (minute < 30) {
                                minutes[38]++;
                            } else {
                                minutes[39]++;
                            }
                            break;
                        case 20:
                            if (minute < 30) {
                                minutes[40]++;
                            } else {
                                minutes[41]++;
                            }
                            break;
                        case 21:
                            if (minute < 30) {
                                minutes[42]++;
                            } else {
                                minutes[43]++;
                            }
                            break;
                        case 22:
                            if (minute < 30) {
                                minutes[44]++;
                            } else {
                                minutes[45]++;
                            }
                            break;
                        case 23:
                            if (minute < 30) {
                                minutes[46]++;
                            } else {
                                minutes[47]++;
                            }
                            break;
                    }
                }

                for (int i = 0; i < minutes.length; i++) {
                    if (minutes[i] >= 10) {
                        Date toDoDate = new Date();
                        toDoDate.setHours(i / 2);
                        if ((i % 2) == 0) {
                            toDoDate.setMinutes(0);
                        } else {
                            toDoDate.setMinutes(30);
                        }
                        toDoDate.setDate(1);
                        ToDoAction toDoAct = new ToDoAction();
                        IAction act = new IAction(actions[a], devices.get(d));
                        todoDao.save(new ToDoAction(toDoDate, act, h, Boolean.FALSE));
                    }
                }
                minutes = new int[48];
                it = weekdaysMorning.iterator();
                while (it.hasNext()) {
                    aux = (HistAction) it.next();
                    hour = aux.getDateTime().getHours();
                    minute = aux.getDateTime().getMinutes();
                    switch (hour) {
                        case 0:
                            if (minute < 30) {
                                minutes[0]++;
                            } else {
                                minutes[1]++;
                            }
                            break;
                        case 1:
                            if (minute < 30) {
                                minutes[2]++;
                            } else {
                                minutes[3]++;
                            }
                            break;
                        case 2:
                            if (minute < 30) {
                                minutes[4]++;
                            } else {
                                minutes[5]++;
                            }
                            break;
                        case 3:
                            if (minute < 30) {
                                minutes[6]++;
                            } else {
                                minutes[7]++;
                            }
                            break;
                        case 4:
                            if (minute < 30) {
                                minutes[8]++;
                            } else {
                                minutes[9]++;
                            }
                            break;
                        case 5:
                            if (minute < 30) {
                                minutes[10]++;
                            } else {
                                minutes[11]++;
                            }
                            break;
                        case 6:
                            if (minute < 30) {
                                minutes[12]++;
                            } else {
                                minutes[13]++;
                            }
                            break;
                        case 7:
                            if (minute < 30) {
                                minutes[14]++;
                            } else {
                                minutes[15]++;
                            }
                            break;
                        case 8:
                            if (minute < 30) {
                                minutes[16]++;
                            } else {
                                minutes[17]++;
                            }
                            break;
                        case 9:
                            if (minute < 30) {
                                minutes[18]++;
                            } else {
                                minutes[19]++;
                            }
                            break;
                        case 10:
                            if (minute < 30) {
                                minutes[20]++;
                            } else {
                                minutes[21]++;
                            }
                            break;
                        case 11:
                            if (minute < 30) {
                                minutes[22]++;
                            } else {
                                minutes[23]++;
                            }
                            break;
                    }
                }
                it = weekdaysAfternoon.iterator();
                while (it.hasNext()) {
                    aux = (HistAction) it.next();
                    hour = aux.getDateTime().getHours();
                    minute = aux.getDateTime().getMinutes();
                    switch (hour) {
                        case 12:
                            if (minute < 30) {
                                minutes[24]++;
                            } else {
                                minutes[25]++;
                            }
                            break;
                        case 13:
                            if (minute < 30) {
                                minutes[26]++;
                            } else {
                                minutes[27]++;
                            }
                            break;
                        case 14:
                            if (minute < 30) {
                                minutes[28]++;
                            } else {
                                minutes[29]++;
                            }
                            break;
                        case 15:
                            if (minute < 30) {
                                minutes[30]++;
                            } else {
                                minutes[31]++;
                            }
                            break;
                        case 16:
                            if (minute < 30) {
                                minutes[32]++;
                            } else {
                                minutes[33]++;
                            }
                            break;
                        case 17:
                            if (minute < 30) {
                                minutes[34]++;
                            } else {
                                minutes[35]++;
                            }
                            break;
                        case 18:
                            if (minute < 30) {
                                minutes[36]++;
                            } else {
                                minutes[37]++;
                            }
                            break;
                        case 19:
                            if (minute < 30) {
                                minutes[38]++;
                            } else {
                                minutes[39]++;
                            }
                            break;
                        case 20:
                            if (minute < 30) {
                                minutes[40]++;
                            } else {
                                minutes[41]++;
                            }
                            break;
                        case 21:
                            if (minute < 30) {
                                minutes[42]++;
                            } else {
                                minutes[43]++;
                            }
                            break;
                        case 22:
                            if (minute < 30) {
                                minutes[44]++;
                            } else {
                                minutes[45]++;
                            }
                            break;
                        case 23:
                            if (minute < 30) {
                                minutes[46]++;
                            } else {
                                minutes[47]++;
                            }
                            break;
                    }
                }
                for (int i = 0; i < minutes.length; i++) {
                    if (minutes[i] >= 50) {
                        Date toDoDate = new Date();
                        toDoDate.setHours(i / 2);
                        if ((i % 2) == 0) {
                            toDoDate.setMinutes(0);
                        } else {
                            toDoDate.setMinutes(30);
                        }
                        toDoDate.setDate(2);
                        ToDoAction toDoAct = new ToDoAction();
                        IAction act = new IAction(actions[a], devices.get(d));
                        todoDao.save(new ToDoAction(toDoDate, act, h, Boolean.FALSE));
                    }
                }
            }
        }
    }
}

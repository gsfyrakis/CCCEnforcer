package uk.ac.ncl.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import uk.ac.ncl.event.Event;
import uk.ac.ncl.event.EventStatus;
import uk.ac.ncl.event.Operation;
import uk.ac.ncl.event.Operation.OperationName;
import uk.ac.ncl.dataBo.DataBo;
import uk.ac.ncl.model.Data;
import uk.ac.ncl.response.CCCResponse;
import uk.ac.ncl.toolBo.ToolBo;
import uk.ac.ncl.user.User;
import uk.ac.ncl.util.DateParser;
import uk.ac.ncl.amazon.S3;
import uk.ac.ncl.core.CCCEngine;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/data/*")
public class DataController {
    @Autowired
    private DataBo db;

    @Autowired
    private CCCEngine engine;

    @Autowired
    private ToolBo tb;

    @Autowired
    private S3 s3;

    private final String DATA = "data";

    private final String TOOL = "tool";

	/*
     * private javax.transaction.TransactionManager transactionManager =
	 * com.arjuna.ats.jta.TransactionManager .transactionManager(); private
	 * javax.transaction.UserTransaction userTransaction = new
	 * ServerVMClientUserTransaction( transactionManager);
	 */

    @RequestMapping(value = {"/upload"}, method = {RequestMethod.GET})
    public String upload(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            return "upload";
        }
    }

    @RequestMapping(value = {"/update"}, method = {RequestMethod.GET})
    public String update(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            return "update";
        }
    }

    @RequestMapping(value = {"/retrieve"}, method = {RequestMethod.GET})
    public String retrieve(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            return "retrieve";
        }
    }

    @RequestMapping(value = {"/dependency"}, method = {RequestMethod.GET})
    public String dependency(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            return "dependency";
        }
    }

    @RequestMapping(value = {"/remove"}, method = {RequestMethod.GET})
    public String remove(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            return "remove";
        }
    }

    @Transactional
    @RequestMapping(value = {"/upload"}, method = {RequestMethod.POST}, params = {"type"})
    public String upload(HttpServletRequest req, Model model,
                         @RequestParam("uploadedFile") CommonsMultipartFile file)
            throws IOException, ServletException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        }
        if (file.isEmpty()) {
            return "fail";
        } else {
            User user = (User) session.getAttribute("user");
            String userName = user.getName();
            String fileName = file.getOriginalFilename();
            String type = req.getParameter("type");
            String key = "uk/ac/ncl/ReseachDataRepository/" + fileName;
            Operation operation = new Operation(OperationName.upload, type, key);
            Event event = new Event(userName, operation);
            CCCResponse response = engine.run(event);
            Boolean verdict = response.getContractCompliant();
            if (verdict) {
                try {
                    ObjectMetadata meta = new ObjectMetadata();
                    long size = file.getSize();
                    meta.setContentLength(size);
                    String fileType = new ConfigurableMimeFileTypeMap()
                            .getContentType(fileName);
                    meta.setContentType(fileType);
                    s3.upload(file.getInputStream(), key, meta);
                    if (type.equals(DATA)) {
                        db.create(fileName, userName, (long) size, key);
                        event.setStatus(EventStatus.succeed);
                        engine.run(event);
                        model.addAttribute("key", key);
                        return "uploadSuccess";
                    } else if (type.equals(TOOL)) {
                        tb.create(fileName, userName, (long) size, key);
                        event.setStatus(EventStatus.succeed);
                        engine.run(event);
                        model.addAttribute("key", key);
                        return "uploadSuccess";
                    } else
                        return "fail";
                } catch (Exception e) {
                    e.printStackTrace();
                    event.setStatus(EventStatus.failed);
                    engine.run(event);
                    return "fail";
                }
            } else {
                event.setStatus(EventStatus.nonContractCompliant);
                return "reject";
            }
        }
    }

    @Transactional
    @RequestMapping(value = {"/retrieve"}, method = {RequestMethod.POST}, params = {
            "key", "date"})
    public String update(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            User user = (User) session.getAttribute("user");
            String userName = user.getName();
            String key = req.getParameter("key");
            String dateStr = req.getParameter("date");
            Operation operation = new Operation(OperationName.update, DATA, key);
            Event event = new Event(userName, operation);
            CCCResponse response = engine.run(event);
            Boolean verdict = response.getContractCompliant();
            if (verdict) {
                try {
                    Date date = DateParser.simpleParse(dateStr);
                    db.update(key, date);
                    event.setStatus(EventStatus.succeed);
                    engine.run(event);
                    return "success";
                } catch (Exception e) {
                    e.printStackTrace();
                    event.setStatus(EventStatus.failed);
                    engine.run(event);
                    return "fail";
                }
            } else {
                event.setStatus(EventStatus.nonContractCompliant);
                return "reject";
            }
        }
    }

    @Transactional
    @RequestMapping(value = {"/retrieve"}, method = {RequestMethod.POST}, params = {"key"})
    public String retrieve(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            String key = req.getParameter("key");
            try {
                Data data = db.query(key);
                model.addAttribute("data", data);
                return "dataInfo";
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }
        }
    }

    @Transactional
    @RequestMapping(value = {"/dependency"}, method = {RequestMethod.POST}, params = {
            "key", "rawdata", "software"})
    public String addDependency(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            User user = (User) session.getAttribute("user");
            String userName = user.getName();
            String key = req.getParameter("key");
            Operation operation = new Operation(OperationName.addDependency,
                    DATA, key);
            Event event = new Event(userName, operation);
            CCCResponse response = engine.run(event);
            Boolean verdict = response.getContractCompliant();
            if (verdict) {
                try {
                    String[] rawdataArray = req.getParameterValues("rawdata");
                    String[] softwareArray = req.getParameterValues("software");
                    List<String> rawdata = removeNull(rawdataArray);
                    List<String> software = removeNull(softwareArray);
                    for (String result : rawdata)
                        db.addDependency(key, DATA, result);
                    for (String result : software)
                        db.addDependency(key, TOOL, result);
                    event.setStatus(EventStatus.succeed);
                    engine.run(event);
                    return "success";
                } catch (Exception e) {
                    e.printStackTrace();
                    event.setStatus(EventStatus.failed);
                    engine.run(event);
                    return "fail";
                }
            } else {
                event.setStatus(EventStatus.nonContractCompliant);
                return "reject";
            }

        }
    }

    @Transactional
    @RequestMapping(value = {"/remove"}, method = {RequestMethod.POST}, params = {"key"})
    public String remove(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            User user = (User) session.getAttribute("user");
            String userName = user.getName();
            String key = req.getParameter("key");
            Operation operation = new Operation(OperationName.remove, DATA, key);
            Event event = new Event(userName, operation);
            CCCResponse response = engine.run(event);
            Boolean verdict = response.getContractCompliant();
            if (verdict) {
                try {
                    db.delete(key);
                    s3.delete(key);
                    event.setStatus(EventStatus.succeed);
                    engine.run(event);
                    return "removeSuccess";
                } catch (Exception e) {
                    e.printStackTrace();
                    event.setStatus(EventStatus.failed);
                    engine.run(event);
                    return "fail";
                }
            } else {
                event.setStatus(EventStatus.nonContractCompliant);
                return "reject";
            }
        }
    }

    @Transactional
    @RequestMapping(value = {"/download/{name:.*}"}, method = {RequestMethod.GET})
    public String download(HttpServletRequest req, HttpServletResponse resp,
                           @PathVariable("name") String name) throws IOException {

        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            User user = (User) session.getAttribute("user");
            String userName = user.getName();
            String key = "uk/ac/ncl/ReseachDataRepository/" + name;
            Operation operation = new Operation(OperationName.download, DATA,
                    key);
            Event event = new Event(userName, operation);
            CCCResponse response = engine.run(event);
            Boolean verdict = response.getContractCompliant();
            if (verdict) {
                try {
                    final int BUFSIZE = 4096;
                    int length = 0;
                    S3Object file = s3.download(key);
                    ServletOutputStream outStream = resp.getOutputStream();
                    String fileType = file.getObjectMetadata().getContentType();
                    long size = file.getObjectMetadata().getContentLength();
                    resp.setContentType(fileType);
                    resp.setContentLength((int) size);
                    resp.setHeader("Content-Disposition",
                            "attachment; filename=\"" + name + "\"");
                    byte[] byteBuffer = new byte[BUFSIZE];
                    DataInputStream in = new DataInputStream(
                            file.getObjectContent());
                    while ((in != null)
                            && ((length = in.read(byteBuffer)) != -1)) {
                        outStream.write(byteBuffer, 0, length);
                    }

                    in.close();
                    outStream.close();
                    event.setStatus(EventStatus.succeed);
                    engine.run(event);
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    event.setStatus(EventStatus.failed);
                    engine.run(event);
                    return "fail";
                }
            } else {
                event.setStatus(EventStatus.nonContractCompliant);
                return "reject";
            }
        }
    }

    private static List<String> removeNull(String[] array) {
        List<String> answer = new ArrayList<String>();
        for (String result : array) {
            if (result.length() != 0) {
                answer.add(result);
            }
        }
        return answer;
    }
}
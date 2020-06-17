package cn.edu.bnuz;

import cn.edu.bnuz.entity.Person;
import com.sun.deploy.util.StringUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 控制层
 */
public class Controller {
    @FXML
    private TableView<Person> tableView;

    @FXML
    private TableColumn<Person, String> idColumn;

    @FXML
    private TableColumn<Person, String> nameColumn;

    @FXML
    private TableColumn<Person, String> phoneColumn;

    @FXML
    private TableColumn<Person, String> emailColumn;

    @FXML
    private TableColumn<Person, Boolean> selectedColumn;

    @FXML
    private TextField idAdd;

    @FXML
    private TextField nameAdd;

    @FXML
    private TextField phoneAdd;

    @FXML
    private TextField emailAdd;

    @FXML
    private TextField idModify;

    @FXML
    private TextField nameModify;

    @FXML
    private TextField phoneModify;

    @FXML
    private TextField emailModify;


    @FXML
    private TextField idSearch;

    @FXML
    private TextField nameSearch;

    private boolean index = false;

    //表格内容list
    private final ObservableList<Person> data = FXCollections.observableArrayList();

    //文件目录
    private final Path path = Paths.get("src/test.txt");

    //与表格内容同步的数据list
    private final List<Person> personList = new ArrayList<>();
    private final List<Person> tmp = new ArrayList<>();

    /**
     * list转byte[]数组，用于存入文件中
     *
     * @param personList
     * @return
     */
    public byte[] listToBytes(List<Person> personList) {
        StringBuffer sb = new StringBuffer();
        for (Person person : personList) {
            sb.append(person.toString());
        }
        return sb.toString().getBytes();
    }

    /**
     * 新增
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void add(ActionEvent event) throws IOException {
        if (idAdd.getText() != null
                && nameAdd.getText() != null
                && phoneAdd.getText() != null
                && emailAdd.getText() != null) {
            //获得文本框的输入
            Person person = new Person(
                    idAdd.getText(),
                    nameAdd.getText(),
                    phoneAdd.getText(),
                    emailAdd.getText());
            //添加到表格中
            data.add(person);
            //同步添加到list中
            personList.add(person);

            System.out.println("[add]: " + idAdd.getText() + " " + nameAdd.getText() + " " + phoneAdd.getText() + " " + emailAdd.getText());

            //将list写入到文件中
            Files.write(path, listToBytes(personList));

            //清空文本输入框
            idAdd.clear();
            nameAdd.clear();
            phoneAdd.clear();
            emailAdd.clear();
        }
    }

    /**
     * 删除
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void delete(ActionEvent event) throws IOException {
        deleteStudents();
    }

    private boolean deleteStudents() throws IOException {
        int size = data.size();
        if (size <= 0) {
            return false;
        }
        //遍历所选选项
        for (int i = size - 1; i >= 0; i--) {
            Person p = data.get(i);
            if (p.isSelected()) {

                System.out.println("[Delete]: " + p.getId() + " " + p.getName() + " " + p.getPhone() + " " + p.getEmail());
                //从list中移除
                personList.remove(p);
                //将list写入到文件中
                Files.write(path, listToBytes(personList));
                //从表格中移除
                data.remove(p);
            }
        }
        return true;
    }

    /**
     * 修改
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void modify(ActionEvent event) throws IOException {
        boolean flag = false;
        if (idModify.getText() != null
                && nameModify.getText() != null
                && phoneModify.getText() != null
                && emailModify.getText() != null) {

            //遍历list，判断是否有同将要修改id一致的元素
            for (Person p : personList) {
                if (p.getId().equals(idModify.getText())) {
                    flag = true;
                    //先移除列表所有数据
                    data.removeAll(personList);
                    //更新改行数据
                    p.setId(idModify.getText());
                    p.setName(nameModify.getText());
                    p.setPhone(phoneModify.getText());
                    p.setEmail(phoneModify.getText());

                    System.out.println("[modify]: " + p.getId() + " " + p.getName() + " " + p.getPhone() + " " + p.getEmail());
                }
            }
            //如果有更新，则添加全部数据回表格
            if (flag) {
                data.addAll(personList);
            }

            Files.write(path, listToBytes(personList));

            idModify.clear();
            nameModify.clear();
            phoneModify.clear();
            emailModify.clear();
        }
    }

    /**
     * 查找，仅支持通过id或name查找
     *
     * @param event
     * @throws CloneNotSupportedException
     */
    @FXML
    void search(ActionEvent event) throws CloneNotSupportedException {
        for (Person p : personList) {
            tmp.add(p.clone());
        }
        //先清空表格所有数据
        data.removeAll(personList);

        String id = idSearch.getText();
        String name = nameSearch.getText();

        //若为空时，则添加回所有数据
        if (id.equals("") && name.equals("")) {
            data.addAll(personList);
        }

        //遍历list，若有与搜索条件相同的数据，则添加到表格中
        for (Person p : personList) {
            if (p.getId().equals(id) || p.getName().equals(name)) {
                data.add(p);
            }
        }
        idSearch.clear();
        nameSearch.clear();
    }

    @FXML
    private void initialize() throws IOException {
        //绑定每一列
        idColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("id"));
        selectedColumn.setCellValueFactory(new PropertyValueFactory<Person, Boolean>("selected"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));
        //设定数据list
        tableView.setItems(data);
        tableView.setEditable(true);

        //按行读取文件，存入list中
        List<String> lines = Files.readAllLines(path);
        //根据分隔符拆分文件每一行数据，存入person对象中，并添加到fx表格中
        for (String line : lines) {
            String[] tmp = line.split(",");
            personList.add(new Person(tmp[0], tmp[1], tmp[2], tmp[3]));
            //不能通过匿名对象创建，会导致无法remove掉元素
            data.add(personList.get(personList.size() - 1));
        }

        //初始化多选框，默认全为false，当为true则被选中
        selectedColumn.setCellFactory(
                CellFactory.tableCheckBoxColumn(new Callback<Integer, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(Integer index) {
                        final Person person = tableView.getItems().get(index);
                        ObservableValue<Boolean> ret =
                                new SimpleBooleanProperty(person, "selected", person.isSelected());
                        ret.addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(
                                    ObservableValue<? extends Boolean> observable,
                                    Boolean oldValue, Boolean newValue) {
                                person.setSelected(newValue);
                            }
                        });
                        return ret;
                    }
                }));

        /**
         * 单元格都可修改，但暂时未能保存到文件中，为后续优化功能
         */
        //每个单元格都可编辑
        Callback<TableColumn<Person, String>,
                TableCell<Person, String>> cellFactory
                = (TableColumn<Person, String> p) -> new EditingCell();

        //为每列设定cellFactory
        idColumn.setCellFactory(cellFactory);
        nameColumn.setCellFactory(cellFactory);
        phoneColumn.setCellFactory(cellFactory);
        emailColumn.setCellFactory(cellFactory);

        //设置每个单元格可修改
        idColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Person, String> t) -> {
                    ((Person) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
                });
        //设置每个单元格可修改
        nameColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Person, String> t) -> {
                    ((Person) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
                });
        //设置每个单元格可修改
        phoneColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Person, String> t) -> {
                    ((Person) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPhone(t.getNewValue());
                });
        //设置每个单元格可修改
        emailColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Person, String> t) -> {
                    ((Person) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setEmail(t.getNewValue());
                });

        System.out.println("[initialize]: " + idColumn.getText() + " " + nameColumn.getText() + " " + phoneColumn.getText() + " " + emailColumn.getText());

    }
}

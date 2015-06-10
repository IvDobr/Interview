function User (userId, userLogin, userFirstName, userLastName, userPass, userStatus, userReg) {
    var self = this;
    self.userId             = userId;
    self.userLogin          = ko.observable(userLogin);
    self.userFirstName      = ko.observable(userFirstName);
    self.userLastName       = ko.observable(userLastName);
    self.userPass           = ko.observable(userPass);
    self.userStatus         = ko.observable(userStatus);
    self.userReg            = ko.observable(userReg);
}

ViewModelUsers = function() {

    var self = this;

    self.usersList          = ko.observableArray([]);

    self.editUserId             = ko.observable("");
    self.editUserPass           = ko.observable("");
    self.editUserFirstName      = ko.observable("");
    self.editUserLastName       = ko.observable("");
    self.editUserStatus         = ko.observable("");
    self.editUserLogin          = ko.observable("");

    self.userId             = ko.observable("");
    self.userPass           = ko.observable("");
    self.userName           = ko.observable("");
    self.userFirstName      = ko.observable("");
    self.userLastName       = ko.observable("");
    self.userStatus         = ko.observable("");
    self.userLogin          = ko.observable("");
    self.userReg            = ko.observable("");

    self.pageSize           = ko.observable(20);
    self.currentPage        = ko.observable(1);
    self.countUsers         = ko.observable("");
    self.totalPages         = ko.observable("");
    self.pages              = ko.observableArray([]);
    self.search             = ko.observable("");
    self.sorter             = ko.observable("");

    self.loadUsers = function() {
        jsRoutes.controllers.API.getAllUsersJSON().ajax({
            dataType : 'json',
            contentType : 'charset=utf-8',
            data : {pageSize: parseInt(self.pageSize()), currentPage: parseInt(self.currentPage()), sortby: self.sorter, search: self.search},
            success : function(data) {
                self.countUsers(data.countUsers);
                self.totalPages(data.pages);

                var c = parseInt(self.currentPage());

                self.pages.removeAll();
                for (var j = 1; j <= data.pages; j++) self.pages.push(j);

                self.currentPage(c);

                self.usersList.removeAll();
                var o = data.users;
                for (var i = 0; i < o.length; i++) {
                    self.usersList.push(
                        new User(
                            o[i].userId,
                            o[i].userLogin,
                            o[i].userFirstName,
                            o[i].userLastName,
                            o[i].userPass,
                            o[i].status,
                            o[i].userReg
                        )
                    );
                }
            },
            error : function() {
                console.log('Не могу отправить json запрос');
            }
        });
    };

    self.loadOneUser = function(user) {
        self.editUserId(user.userId);
        self.editUserPass(user.userPass());
        self.editUserFirstName(user.userFirstName());
        self.editUserLastName(user.userLastName());
        self.editUserStatus(user.userStatus()==="Администратор");
        self.editUserLogin(user.userLogin());
        $('#editUser').modal('show');
    };

    self.editUser = function(){
        jsRoutes.controllers.API.editUserJSON().ajax({
            dataType    : 'json',
            contentType : 'application/json; charset=utf-8',
            data        : JSON.stringify({userId:               self.editUserId(),
                editUserPass:         self.editUserPass(),
                editUserFirstName:    self.editUserFirstName(),
                editUserLastName:     self.editUserLastName(),
                editUserLogin:        self.editUserLogin(),
                editUserStatus:       self.editUserStatus()
            }),
            success : function(){
                self.reloadUsers();
            },
            error : function(){
                alert("Не удалось изменить пользователя");
            }
        });
    };

    self.removeUser = function(user){
        if (confirm("Вы действительно хотите удалить пользователя?"))
        jsRoutes.controllers.API.removeUserJSON().ajax({
            dataType    : 'json',
            contentType : 'application/json; charset=utf-8',
            data        : JSON.stringify({userId : user.userId}),
            success : function(){
                self.usersList.remove(user);
            },
            error : function(){
                alert("Не удалось удалить пользователя");
            }
        });
    };

    self.reloadUsers = function() {
        self.usersList.removeAll();
        self.loadUsers();
    };

    self.loadUsers();

    ko.computed(function() {
        self.reloadUsers();
        return self.pageSize() + self.sorter();
    });
};

$( document ).ready(function() {
    ko.applyBindings(new ViewModelUsers());
});
$(function () {
    $('[data-toggle="tooltip"]').tooltip()
});
$('#editUser').on('shown.bs.modal', function () {
    $('#myInput').focus()
});
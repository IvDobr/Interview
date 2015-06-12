function Interview (interviewId, interviewTitle, interviewDate, interviewQcount, interviewAcount, interviewURL) {
    var self = this;
    self.interviewId = interviewId;
    self.interviewTitle = ko.observable(interviewTitle);
    self.interviewDate = ko.observable(interviewDate);
    self.interviewQcount = ko.observable(interviewQcount);
    self.interviewAcount = ko.observable(interviewAcount);
    self.interviewURL = ko.observable(interviewURL);
}

InterviewList = function() {
    var self = this;

    self.search             = ko.observable("");
    self.sorter             = ko.observable("");

    self.interviewList      = ko.observableArray([]);

    self.interviewTitle     = ko.observable("");
    self.interviewDate      = ko.observable("");
    self.interviewQcount    = ko.observable("");
    self.interviewAcount    = ko.observable("");
    self.interviewURL       = ko.observable("");

    self.getList = function(){
        jsRoutes.controllers.API.getInterviewJSON().ajax({
            dataType : 'json',
            contentType : 'charset=utf-8',
            data : {sortby: self.sorter, search: self.search},
            success : function(data) {
                var o = data.interviewList;
                self.interviewList.removeAll();
                for (var i = 0; i < o.length; i++) {
                    self.interviewList.push(
                        new Interview(
                            o[i].interviewId,
                            o[i].interviewTitle,
                            o[i].interviewDate,
                            o[i].interviewQcount,
                            o[i].interviewAcount,
                            "interviews.herokuapp.com/interview/" + o[i].interviewId
                        )
                    );
                }
            },
            error : function() {
                console.log('Не могу отправить json запрос');
            }
        });
    };

    self.removeInterview = function(interview){
    if (confirm("Вы действительно хотите удалить опрос?"))
        jsRoutes.controllers.API.removeInterviewJSON().ajax({
            dataType    : 'json',
            contentType : 'application/json; charset=utf-8',
            data        : JSON.stringify({id : interview.interviewId}),
            success : function(){
                self.interviewList.remove(interview);
            },
            error : function(){
                alert("Не удалось удалить опрос");
            }
        });
    };

    self.editInterview = function(){};

    self.answers = function(interview){
        document.location.href = "http://interviews.herokuapp.com/answers?id="+interview.interviewId;
    };

    ko.computed(function() {
        self.getList();
        return self.sorter();
    });

    self.getList();
};

$( document ).ready(function() {
    ko.applyBindings(new InterviewList());
});

$(function () {
    $('[data-toggle="tooltip"]').tooltip()
});
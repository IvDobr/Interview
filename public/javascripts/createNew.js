var InterviewModel = function() {
    var self = this;

    self.title = ko.observable("");
    self.description = ko.observable("");
    self.reqUserName = ko.observable(false);
    self.intURL = ko.observable(null);

    self.questions = ko.observableArray();

    self.addQuestion = function() {
        if (100 < self.questions.length) {alert("Слишком много вопросов!")} else {
            self.questions.push({
                question: "",
                required: false,
                manyVars: false,
                userVar: false,
                variants: ko.observableArray()
            });
        }
    };

    self.removeQuestion = function(question) {
        self.questions.remove(question);
    };

    self.addVariant = function(question) {
        question.variants.push({
            variant: ""
        });
    };

    self.removeVariant = function(variant) {
        $.each(self.questions(), function() { this.variants.remove(variant) })
    };

    self.create = function() {
        var dataJSON = JSON.stringify({
            title: self.title(),
            description: self.description(),
            reqUserName: self.reqUserName(),
            questions: ko.toJS(self.questions)
        });
        jsRoutes.controllers.API.newInterviewJSON().ajax({
            dataType    : 'json',
            contentType : 'application/json; charset=utf-8',
            data        : dataJSON,
            success : function(result){
                self.intURL("interviews.herokuapp.com/interview/" + result.id);
                $('#created').modal('show');
            },
            error : function(result){
                console.log("Error: " + result);
            }
        });
    };
};

$( document ).ready(function() {
    ko.applyBindings(new InterviewModel());
});
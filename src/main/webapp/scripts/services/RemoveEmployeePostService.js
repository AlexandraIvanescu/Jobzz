angular.module('jobzz').service('removeEmployeePostService', function () {
    var post = {};
    var postIsDeleted = false;

    var setPost = function (newObj) {
        post = newObj;
    };

    var getPost = function () {
        return post;
    };

    var setPostIsDeleted = function (value) {
        postIsDeleted = value;
    };

    var getPostIsDeleted = function () {
        return postIsDeleted;
    };

    return {
        setPost: setPost,
        getPost: getPost,
        setPostIsDeleted: setPostIsDeleted,
        getPostIsDeleted: getPostIsDeleted
    };

});

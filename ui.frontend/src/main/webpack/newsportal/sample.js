var name;
var history = workflowSession.getHistory(workItem.getWorkflow());
for (var index = history.size() - 1; index >= 0; index--) {
    var previous = history.get(index);
    var tempRejectApprove = previous.getWorkItem().getMetaDataMap().get('name');
    if((tempRejectApprove != '') && (tempRejectApprove != null)) {
        name = tempRejectApprove;
        break;
    }
}
workItem.getWorkflowData().getMetaData().put('name', name);

var name;
var history = workflowSession.getHistory(workItem.getWorkflow());
for(var index = history.size()-1; index >= 0; index--){
    var previous = history.get(index);
    var tempRejectApprove = previous.getWorkItem().getMetaDataMap().get('name');
    if((tempRejectApprove != '') && (tempRejectApprove != null)){
        name = tempRejectApprove;
        break;
    }
}
workItem.getWorkflowData().getMetaData.put('name',name);



function check() {
    var match = 'approve';
    if (workflowData.getMetaData().get('name') == match) {
        return true;
    } else {
        return false;
    }
} 

function check() {
    var match = 'rejected';
    if (workflowData.getMetaData().get('name') == match) {
        return true;
    } else {
        return false;
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ISS.MV2.Messaging;

namespace ISS.MV2.Threading {

    public delegate void ProcedureUpdateDelegate<P, R>(MessageProcedure<P, R> sender, string state, int progress);
    public delegate void ProcedureFailedDelegate<P, R>(MessageProcedure<P, R> sender, Exception ex);
    public delegate void ProcedureCompletedDelegate<P, R>(MessageProcedure<P, R> sender, R result);
    public delegate void ProcedureEndedDelegate<P, R>(MessageProcedure<P, R> sender);


    public abstract class MessageProcedure<P, R> : IProcedureListener<P, R> {

        public event ProcedureCompletedDelegate<P, R> Completed;
        public event ProcedureFailedDelegate<P, R> Failed;
        public event ProcedureUpdateDelegate<P, R> Updated;
        public event ProcedureEndedDelegate<P, R> Ended;

        

        private bool running = false;
        private object lock_object = new object();

        public bool IsRunning { get { return running; } }

        private IEventDispatcher eventDispatcher;

        public IEventDispatcher EventDispatcher {
            get {
                return eventDispatcher;
            }
            set {
                eventDispatcher = value;
            }
        }

        private P parameter;

        private string state = "";
        private int progress = 0;

        public string State { get { return state; } }
        public int Progress { get { return progress; } }

        protected P Parameter {
            get {
                return parameter;
            }
            set {
                parameter = value;
            }
        }

        public MessageProcedure(IEventDispatcher dispatcher, P parameter) {
            this.eventDispatcher = dispatcher;
            this.parameter = parameter;
        }


        protected abstract R DoProcedure(P parameter);

        private void NotifyUpdate() {
            if (Updated == null) return;
            eventDispatcher.Invoke(new EventInvokationDelegate(() => {
                Updated(this, state, progress);
            }));
        }

        protected void Update(int progress) {
            this.progress = progress;
            NotifyUpdate();
        }

        protected void Update(string state) {
            this.state = state;
            NotifyUpdate();
        }

        protected void Update(string state, int progress) {
            this.state = state;
            this.progress = progress;
            NotifyUpdate();
        }

        public void Execute() {
            lock (lock_object) {
                if (running) return;
                running = true;
                Task t = new Task(new Action<object>((param) => {
                    try {
                        R result = DoProcedure((P)param);
                        NotifySuccess(result);
                    } catch(Exception ex) {
                        NotifyFail(ex);
                    }
                    lock (lock_object) {
                        running = false;
                    }
                    NotifyEneded();
                }), parameter);
                t.Start();
            }
        }

        private void NotifyEneded() {
            if (Ended == null) return;
            eventDispatcher.Invoke(new EventInvokationDelegate(() => {
                Ended(this);
            }));
        }

        private void NotifySuccess(R result) {
            if (Completed == null) return;
            eventDispatcher.Invoke(new EventInvokationDelegate(() => {
                Completed(this, result);
            }));
        }

        private void NotifyFail(Exception ex) {
            if (Failed == null) return;
            eventDispatcher.Invoke(new EventInvokationDelegate(() => {
                Failed(this, ex);
            }));
        }

        public R ExecuteImmediate() {
            return DoProcedure(parameter);
        }

        protected T AssertTypeAndConvert<T>(MV2Message message, T destination) where T : MV2Message {
            if (destination.MessageType != message.MessageType) {
                CheckFail(message);
                throw new InvalidResponseMessageException(destination.MessageType, message.MessageType);
            }
            MV2Message.Merge(destination, message);
            return destination;
        }

        protected void AssertType(MV2Message message, DEF_MESSAGE type) {
            if (type != message.MessageType) {
                CheckFail(message);
                throw new InvalidResponseMessageException(type, message.MessageType);
            }
        }

        protected void CheckFail(MV2Message message) {
            if (message.MessageType == DEF_MESSAGE.UNABLE_TO_RPOCESS) {
                throw new RequestException("The server responeded with: " + message.GetFieldStringValue(DEF_MESSAGE_FIELD.CAUSE, ""));
            }
        }

        public void AddListener(IProcedureListener<P, R> listener) {
            Completed += listener.Completed;
            Failed += listener.Failed;
            Updated += listener.Updated;
        }


        void IProcedureListener<P, R>.Completed(MessageProcedure<P, R> sender, R result) {
            NotifySuccess(result);
        }

        void IProcedureListener<P, R>.Updated(MessageProcedure<P, R> sender, string state, int progress) {
            this.state = state;
            this.progress = progress;
            NotifyUpdate();
        }

        void IProcedureListener<P, R>.Failed(MessageProcedure<P, R> sender, Exception exception) {
            NotifyFail(exception);
        }
    }

}
